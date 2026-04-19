const xlsx = require('xlsx');
const path = require('path');
const axios = require('axios');

// Configuraciones
const API_URL = 'http://localhost:8080/api';
const FILE_PATH = path.resolve('C:\\Users\\ramiro.bociga\\Documents\\Proyecto MajoStep\\majo\'step.xlsx');
// Credenciales de administrador - ¡Cámbialas según tu base de datos local!
const AUTH_CREDENTIALS = {
    username: 'admin', 
    password: 'password'
};

const BATCH_SIZE = 5; // Cargar de a 5 para no saturar 

async function start() {
    try {
        console.log("Iniciando sesión...");
        const loginResponse = await axios.post(`${API_URL}/auth/login`, AUTH_CREDENTIALS, {
            validateStatus: () => true
        });

        if (loginResponse.status !== 200) {
            console.error("Error al iniciar sesión. Código:", loginResponse.status);
            console.log("Respuesta:", loginResponse.data);
            console.log("Por favor actualiza AUTH_CREDENTIALS en este script.");
            return;
        }

        const token = loginResponse.data.token || loginResponse.data.jwt; 
        if (!token) {
            // Ajustar según el formato real de tu respuesta de login
            console.error("No se encontró el token en la respuesta:", loginResponse.data);
            return;
        }
        console.log("Login exitoso, leyendo Excel...");

        const workbook = xlsx.readFile(FILE_PATH);
        const sheet = workbook.Sheets[workbook.SheetNames[0]];
        const data = xlsx.utils.sheet_to_json(sheet, { header: 1 });

        // Filtrar filas vacías o inválidas, omitiendo los de encabezado (hasta índice 5 inclusive)
        const rows = data.slice(6).filter(row => row.length > 0 && row[0]);
        console.log(`Encontrados ${rows.length} productos para importar.`);

        const headers = { Authorization: `Bearer ${token}` };
        let exitosos = 0;
        let fallidos = 0;

        for (let i = 0; i < rows.length; i++) {
            const row = rows[i];
            
            // Mapeo (Ajustar categoriaId según tu BD)
            // row[0]=Ref, row[1]=Nombre, row[2]=Cat, row[3]=Talla, row[4]=Costo, row[5]=Precio, row[6]=Unidades, row[7]=SKU, row[8]=Desc
            const productoReq = {
                referencia: row[0] ? String(row[0]) : "GEN-" + i,
                nombre: row[1] ? String(row[1]) : "Sin Nombre",
                categoriaId: 1, // <--- CAMBIAR esto dependiendo de cómo asignes categorías o hacer una búsqueda previa
                talla: row[3] ? String(row[3]) : "",
                precioCompra: parseFloat(row[4]) || 0,
                precioVenta: parseFloat(row[5]) || 0,
                stockActual: parseInt(row[6]) || 0,
                stockMinimo: 0,
                descripcion: row[8] ? String(row[8]) : "",
                activo: String(row[9]).toLowerCase().includes('no') ? false : true,
                imagenUrl: ""
            };

            try {
                const res = await axios.post(`${API_URL}/productos`, productoReq, { headers });
                console.log(`[OK] Producto creado: ${productoReq.nombre} (Ref: ${productoReq.referencia})`);
                exitosos++;
            } catch (error) {
                console.error(`[ERROR] Falló ${productoReq.nombre}:`, error.response ? error.response.data : error.message);
                fallidos++;
            }

            // Pequeña pausa opcional
            if (i > 0 && i % BATCH_SIZE === 0) {
                await new Promise(res => setTimeout(res, 500));
            }
        }

        console.log("=== RESUMEN DE CARGA ===");
        console.log(`Exitosos: ${exitosos}`);
        console.log(`Fallidos: ${fallidos}`);
        
    } catch (e) {
        console.error("Error crítico durante la carga:", e.message);
    }
}

start();
