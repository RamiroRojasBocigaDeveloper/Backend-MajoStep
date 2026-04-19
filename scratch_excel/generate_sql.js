const xlsx = require('xlsx');
const path = require('path');
const fs = require('fs');

const FILE_PATH = path.resolve('C:\\Users\\ramiro.bociga\\Documents\\Proyecto MajoStep\\majo\'step.xlsx');
const SQL_PATH = path.resolve(__dirname, 'importar_productos.sql');

try {
    const workbook = xlsx.readFile(FILE_PATH);
    const sheet = workbook.Sheets[workbook.SheetNames[0]];
    const data = xlsx.utils.sheet_to_json(sheet, { header: 1 });

    const rows = data.slice(6).filter(row => row.length > 0 && row[0]);
    
    let sqlContent = `-- Script de inserción de productos para DBeaver\n`;
    sqlContent += `USE MajoStepDB_sharpmedig;\n\n`;

    rows.forEach((row, i) => {
        const ref = String(row[0] || 'GEN-' + i).replace(/'/g, "''");
        const nombre = String(row[1] || 'Sin Nombre').replace(/'/g, "''");
        const categoriaId = 1; // Ajustar si es necesario
        const talla = String(row[3] || '').replace(/'/g, "''");
        const precioCompra = parseFloat(row[4]) || 0;
        const precioVenta = parseFloat(row[5]) || 0;
        const stockActual = parseInt(row[6]) || 0;
        const stockMinimo = 5;
        const descripcion = String(row[8] || '').replace(/'/g, "''").substring(0, 500);
        const activo = String(row[9]).toLowerCase().includes('no') ? 0 : 1;

        sqlContent += `INSERT INTO productos (nombre, referencia, categoria_id, precio_compra, precio_venta, stock_actual, stock_minimo, activo, talla, descripcion, created_at, updated_at) VALUES ('${nombre}', '${ref}', ${categoriaId}, ${precioCompra}, ${precioVenta}, ${stockActual}, ${stockMinimo}, ${activo}, '${talla}', '${descripcion}', NOW(), NOW());\n`;
    });

    fs.writeFileSync(SQL_PATH, sqlContent);
    console.log(`¡Archivo SQL generado exitosamente con ${rows.length} registros!`);
    console.log(`Ruta: ${SQL_PATH}`);
} catch (err) {
    console.error("Error validando Excel:", err.message);
}
