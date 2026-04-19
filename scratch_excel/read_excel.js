const xlsx = require('xlsx');
const path = require('path');

const filePath = path.resolve('C:\\Users\\ramiro.bociga\\Documents\\Proyecto MajoStep\\majo\'step.xlsx');
try {
    const workbook = xlsx.readFile(filePath);
    const sheetName = workbook.SheetNames[0];
    const sheet = workbook.Sheets[sheetName];
    // Start reading from row 5 (index 4) - xlsx module skips empty rows by default
    const data = xlsx.utils.sheet_to_json(sheet, { header: 1 });
    console.log("Row 6 (index 5):", data[5]);
    console.log("Row 7 (index 6):", data[6]);
    console.log("Row 8 (index 7):", data[7]);
} catch (err) {
    console.error("Error reading file:", err);
}
