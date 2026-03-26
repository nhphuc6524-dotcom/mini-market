const api = "/api/products"
let products = []
let categories = []

let currentPage = 1
let rowsPerPage = 6
let units = []; // Thêm biến này ở đầu file
let sortField = null
let sortAsc = true
function authorizedFetch(url, options = {}) {
    const user = JSON.parse(localStorage.getItem("user") || "{}");
    const token = user.token; // nếu bạn lưu JWT ở đây

    if (!options.headers) options.headers = {};
    if (token) options.headers["Authorization"] = "Bearer " + token;

    return fetch(url, options).then(res => {
        if (!res.ok) throw new Error(res.status + " " + res.statusText);
        return res.json();
    });
}
function loadProducts() {
    authorizedFetch("/api/products")
    .then(data => {
        products = data;
        currentPage = 1;
        displayProducts();
    });
}

function loadUnits() {
    fetch('/api/units')
        .then(res => res.json())
        .then(data => {
            units = data; // Lưu lại để dùng cho hàm getUnitName
            let options = '<option value="">-- Đơn vị --</option>';
            data.forEach(u => {
                options += `<option value="${u.id}">${u.name}</option>`;
            });
            document.getElementById('unit').innerHTML = options;
        });
}

function getUnitName(id) {
    let u = units.find(x => x.id == id);
    return u ? u.name : "-";
}

function displayProducts(){

let start = (currentPage-1) * rowsPerPage
let end = start + rowsPerPage

let pageItems = products.slice(start,end)
let rows = ""

pageItems.forEach(p => {

let cost = p.costPrice || 0
let price = p.price || 0
let stock = p.stockQuantity || 0
let profit = price - cost
let profitColor = profit > 0 ? "text-success" : "text-danger"

let categoryName = getCategoryName(p.categoryId)
let unitName = p.unit ? p.unit.name : (p.unitId ? getUnitName(p.unitId) : "-");
const stockColor = stock <= 10
? 'bg-danger-subtle text-danger'
: 'bg-success-subtle text-success'

rows += `
        <tr>
            <td>
                <div class="fw-semibold">${p.name}</div>
                <small class="text-muted">ID: #${p.id}</small>
            </td>
            <td><span class="badge bg-primary">${categoryName || "Không có"}</span></td>
            <td><span class="badge bg-secondary">${unitName}</span></td> <td><code>${p.barcode || ""}</code></td>
            <td>${Number(cost).toLocaleString("vi-VN")} đ</td>
            <td class="fw-bold">${Number(price).toLocaleString("vi-VN")} đ</td>
            <td class="${profitColor} fw-bold">${Number(profit).toLocaleString("vi-VN")} đ</td>
            <td><span class="stock-badge ${stockColor}">${stock} món</span></td>
            <td class="text-end">
                <button class="btn btn-light btn-sm me-1" onclick="editProduct(${p.id})"><i class="fa-solid fa-pen-to-square"></i></button>
                <button class="btn btn-light btn-sm text-danger" onclick="deleteProduct(${p.id})"><i class="fa-solid fa-trash"></i></button>
            </td>
        </tr>`;
})

document.getElementById("tableBody").innerHTML = rows

let totalPages = Math.ceil(products.length / rowsPerPage)

document.getElementById("pageInfo").innerText =
`Page ${currentPage} / ${totalPages}`

document.getElementById("showing").innerText =
`${products.length} tổng`

}

function generateEAN13(){

let prefix = "893"
let random = Math.floor(100000000 + Math.random() * 900000000)

let base = (prefix + random).substring(0,12)

let sum = 0

for(let i=0;i<12;i++){
let digit = parseInt(base[i])
sum += (i % 2 === 0) ? digit : digit * 3
}

let checkDigit = (10 - (sum % 10)) % 10

return base + checkDigit
}

function saveProduct() {
    // 1. Lấy giá trị từ các ô input
    let id = document.getElementById("id").value;
    let unitIdValue = document.getElementById("unit").value; // Lấy ID từ select

    // 2. Tạo object product để gửi lên server
    let product = {
        name: document.getElementById("name").value,
        barcode: document.getElementById("barcode").value,
        // Loại bỏ dấu chấm phân cách hàng nghìn trước khi gửi số về server
        price: document.getElementById("price").value.replace(/\./g, ''),
        costPrice: document.getElementById("cost").value.replace(/\./g, ''),
        stockQuantity: document.getElementById("stock").value,
        categoryId: document.getElementById("category").value,
        // Gửi unitId dưới dạng số (Integer) để khớp với Backend
        unitId: unitIdValue ? parseInt(unitIdValue) : null
    };

    // 3. Kiểm tra các trường bắt buộc
    if (!product.name || !product.categoryId || !unitIdValue) {
        alert("Vui lòng nhập đầy đủ Tên, Danh mục và Đơn vị!");
        return;
    }

    if (!product.price || !product.stockQuantity) {
        alert("Vui lòng nhập giá bán và số lượng!");
        return;
    }

    // 4. Xử lý Gửi dữ liệu (Cập nhật hoặc Thêm mới)
    if (id) {
        // Cập nhật (PUT)
        fetch(api + "/" + id, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(product)
        })
        .then(res => {
            if (!res.ok) throw new Error("Cập nhật thất bại");
            return res.json();
        })
        .then(() => {
            loadProducts();
            clearForm();
            alert("Cập nhật thành công!");
        })
        .catch(err => alert(err.message));

    } else {
        // Thêm mới (POST)
        fetch(api, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(product)
        })
        .then(res => {
            if (!res.ok) throw new Error("Thêm mới thất bại");
            return res.json();
        })
        .then(() => {
            loadProducts();
            clearForm();
            alert("Thêm sản phẩm thành công!");
        })
        .catch(err => alert(err.message));
    }
}


function editProduct(id) {
    fetch(api + "/" + id)
        .then(res => res.json())
        .then(p => {
            document.getElementById("id").value = p.id;
            document.getElementById("name").value = p.name;
            document.getElementById("barcode").value = p.barcode;
            document.getElementById("price").value = p.price;
            document.getElementById("cost").value = p.costPrice;
            document.getElementById("stock").value = p.stockQuantity;
            document.getElementById("category").value = p.categoryId;
            
            // Đổ dữ liệu vào select Unit
            if(p.unit) {
                document.getElementById("unit").value = p.unit.id;
            } else if(p.unitId) {
                document.getElementById("unit").value = p.unitId;
            }
        });
}


function loadCategories() {
    return fetch("/api/categories")
        .then(res => {
            if(!res.ok) throw new Error("HTTP " + res.status);
            return res.json();
        })
        .then(data => {
            categories = data;
            let options = '<option value="">-- Chọn danh mục --</option>';
            data.forEach(c => {
                options += `<option value="${c.id}">${c.name}</option>`;
            });
            document.getElementById("category").innerHTML = options;
        })
        .catch(err => console.error("Failed to load categories:", err));
}


function getCategoryName(id){

let c = categories.find(x => x.id == id)
return c ? c.name : ""

}


function deleteProduct(id){

if(confirm("Delete product?")){

fetch(api + "/" + id,{
method:"DELETE"
})
.then(()=>loadProducts())

}

}


function clearForm(){

document.getElementById("id").value=""
document.getElementById("name").value=""
document.getElementById("barcode").value = generateEAN13()
document.getElementById("price").value=""
document.getElementById("stock").value=""
document.getElementById("cost").value=""

}


function searchBarcode() {
    let barcode = document.getElementById("search").value.trim();

    if (barcode === "") {
        loadProducts();
        return;
    }

    fetch(api + "/barcode/" + barcode)
        .then(res => {
            if (!res.ok) throw new Error("Not found");
            return res.json();
        })
        .then(p => {
            // Logic màu sắc tồn kho
            const stockColor = p.stockQuantity <= 10
                ? 'bg-danger-subtle text-danger'
                : 'bg-success-subtle text-success';

            // Lấy tên đơn vị và danh mục
            let unitName = p.unit ? p.unit.name : (p.unitId ? getUnitName(p.unitId) : "-");
            let categoryName = getCategoryName(p.categoryId);
            
            // Tính toán lợi nhuận
            let cost = p.costPrice || 0;
            let price = p.price || 0;
            let profit = price - cost;
            let profitColor = profit > 0 ? "text-success" : "text-danger";

            // Tạo hàng dữ liệu (Đảm bảo đủ số lượng cột <td> tương ứng với <th>)
            let row = `
            <tr>
                <td>
                    <div class="fw-semibold">${p.name}</div>
                    <small class="text-muted">ID: #${p.id}</small>
                </td>
                <td><span class="badge bg-primary">${categoryName || "Không có"}</span></td>
                <td><span class="badge bg-secondary">${unitName}</span></td>
                <td><code>${p.barcode || ""}</code></td>
                <td>${Number(cost).toLocaleString("vi-VN")} đ</td>
                <td class="fw-bold">${Number(price).toLocaleString("vi-VN")} đ</td>
                <td class="${profitColor} fw-bold">${Number(profit).toLocaleString("vi-VN")} đ</td>
                <td><span class="stock-badge ${stockColor}">${p.stockQuantity} món</span></td>
                <td class="text-end">
                    <button class="btn btn-light btn-sm me-1" onclick="editProduct(${p.id})">
                        <i class="fa-solid fa-pen-to-square"></i>
                    </button>
                    <button class="btn btn-light btn-sm text-danger" onclick="deleteProduct(${p.id})">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </td>
            </tr>`;

            // Cập nhật giao diện
            document.getElementById("tableBody").innerHTML = row;

            // Reset ô tìm kiếm để chuẩn bị quét mã tiếp theo
            document.getElementById("search").value = "";
            document.getElementById("search").focus();
        })
        .catch(() => {
            alert("Không tìm thấy sản phẩm có mã: " + barcode);
            document.getElementById("search").value = "";
            document.getElementById("search").focus();
        });
}


function nextPage(){

if(currentPage * rowsPerPage < products.length){

currentPage++
displayProducts()

}

}


function prevPage(){

if(currentPage > 1){

currentPage--
displayProducts()

}

}

function sortProducts(field){

if(sortField === field){
sortAsc = !sortAsc
}else{
sortField = field
sortAsc = true
}

products.sort((a,b)=>{

let valA = a[field] || 0
let valB = b[field] || 0

if(typeof valA === "string"){
valA = valA.toLowerCase()
valB = valB.toLowerCase()
}

if(valA > valB) return sortAsc ? 1 : -1
if(valA < valB) return sortAsc ? -1 : 1
return 0

})

displayProducts()

}
function handleBarcode(e){

let barcode = document.getElementById("search").value.trim()

// scanner thường gửi Enter sau khi quét
if(e.key === "Enter" || barcode.length >= 12){

searchBarcode()

}

}
function formatMoney(input){

let value = input.value.replace(/\D/g,'')

if(value === "") return

input.value = Number(value).toLocaleString("vi-VN")

}

// init
loadCategories().then(()=>{
clearForm()
loadUnits();
loadProducts()

})

checkLogin()

window.onload = function(){
document.getElementById("search").focus()
}

let scanner = null

function startCameraScan(){

let scannerDiv = document.getElementById("scanner")

scannerDiv.style.display = "block"

scanner = new Html5Qrcode("scanner")

scanner.start(
{ facingMode: "environment" },
{
fps: 10,
qrbox: 250
},
(barcode)=>{

// khi quét thành công
document.getElementById("search").value = barcode

scanner.stop()
scannerDiv.style.display = "none"

searchBarcode()

},
(error)=>{

// ignore scan error

})

}
function refreshProducts(){

document.getElementById("search").value = ""

loadProducts()

document.getElementById("search").focus()

}

function exportExcelMultiSheet() {
    if(products.length === 0){
        alert("Không có sản phẩm để xuất.");
        return;
    }

    // Hàm tạo worksheet từ dữ liệu
    function createSheet(data, sheetName){
        const ws = XLSX.utils.json_to_sheet(data, {header: ["ID","Tên","Barcode","Danh_mục","Giá Nhập (đ)","Giá Bán (đ)","Lợi Nhuận (đ)","Tồn Kho"]});
        
        // Style header
        const range = XLSX.utils.decode_range(ws['!ref']);
        for(let C = range.s.c; C <= range.e.c; ++C){
            const cell_address = {c:C, r:0};
            const cell_ref = XLSX.utils.encode_cell(cell_address);
            if(!ws[cell_ref]) continue;
            ws[cell_ref].s = {
                font: {bold:true, color: {rgb: "FFFFFF"}},
                fill: {fgColor: {rgb:"4f46e5"}},
                alignment: {horizontal:"center"}
            };
        }

        // Style số tiền & tồn kho
        for(let R = 1; R <= range.e.r; ++R){
            ["E","F","G"].forEach(col => {
                const cell_ref = col + (R+1);
                if(ws[cell_ref]){
                    ws[cell_ref].z = '#,##0 "₫"';
                    ws[cell_ref].s = ws[cell_ref].s || {};
                    ws[cell_ref].s.alignment = {horizontal:"right"};
                }
            });
            const stockCell = "H" + (R+1);
            if(ws[stockCell]){
                ws[stockCell].s = ws[stockCell].s || {};
                ws[stockCell].s.alignment = {horizontal:"right"};
            }
        }

        return ws;
    }

    // Dữ liệu cho các sheet
    const fullData = products.map(p => ({
        ID: p.id,
        Tên: p.name,
        Barcode: p.barcode,
        Danh_mục: getCategoryName(p.categoryId),
        "Giá Nhập (đ)": p.costPrice,
        "Giá Bán (đ)": p.price,
        "Lợi Nhuận (đ)": p.price - p.costPrice,
        "Tồn Kho": p.stockQuantity
    }));

    const lowStockData = fullData.filter(p => p["Tồn Kho"] < 50);

    const topProfitData = fullData
        .slice()
        .sort((a,b) => (b["Lợi Nhuận (đ)"] || 0) - (a["Lợi Nhuận (đ)"] || 0))
        .slice(0, 10);

    // Tạo workbook
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, createSheet(fullData, "Sản phẩm"), "Sản phẩm");
    XLSX.utils.book_append_sheet(wb, createSheet(lowStockData, "Tồn kho thấp"), "Tồn kho thấp");
    XLSX.utils.book_append_sheet(wb, createSheet(topProfitData, "Top lợi nhuận"), "Top lợi nhuận");

    // Xuất file
    XLSX.writeFile(wb, "Bao_cao_san_pham.xlsx");
}