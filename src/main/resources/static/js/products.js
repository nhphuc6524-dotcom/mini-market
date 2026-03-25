const api = "/api/products"
let products = []
let categories = []

let currentPage = 1
let rowsPerPage = 6

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

const stockColor = stock <= 10
? 'bg-danger-subtle text-danger'
: 'bg-success-subtle text-success'

rows += `
<tr>

<td>
<div class="fw-semibold">${p.name}</div>
<small class="text-muted">ID: #${p.id}</small>
</td>

<td>
<span class="badge bg-primary">${categoryName || "Không có"}</span>
</td>

<td>
<code>${p.barcode || ""}</code>
</td>

<td>
${Number(cost).toLocaleString("vi-VN")} đ
</td>

<td class="fw-bold">
${Number(price).toLocaleString("vi-VN")} đ
</td>

<td class="${profitColor} fw-bold">
${Number(profit).toLocaleString("vi-VN")} đ
</td>

<td>
<span class="stock-badge ${stockColor}">
${stock} món
</span>
</td>

<td class="text-end">

<button class="btn btn-light btn-sm me-1" onclick="editProduct(${p.id})">
<i class="fa-solid fa-pen-to-square"></i>
</button>

<button class="btn btn-light btn-sm text-danger" onclick="deleteProduct(${p.id})">
<i class="fa-solid fa-trash"></i>
</button>

</td>

</tr>
`
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

function saveProduct(){

let id = document.getElementById("id").value

let product = {

name: document.getElementById("name").value,
barcode: document.getElementById("barcode").value,

price: document.getElementById("price").value.replace(/\./g,''),
costPrice: document.getElementById("cost").value.replace(/\./g,''),

stockQuantity: document.getElementById("stock").value,
categoryId: document.getElementById("category").value

}

/* ===== THÊM Ở ĐÂY ===== */

if(!product.name){
alert("Vui lòng nhập tên sản phẩm")
return
}

if(!product.categoryId){
alert("Vui lòng chọn danh mục")
return
}

if(!product.price){
alert("Vui lòng nhập giá bán")
return
}

if(!product.stockQuantity){
alert("Vui lòng nhập số lượng")
return
}

/* ===== HẾT PHẦN THÊM ===== */

if(id){

fetch(api + "/" + id,{
method:"PUT",
headers:{"Content-Type":"application/json"},
body:JSON.stringify(product)
})
.then(()=>{
loadProducts()
clearForm()
})

}else{

fetch(api,{
method:"POST",
headers:{"Content-Type":"application/json"},
body:JSON.stringify(product)
})
.then(()=>{
loadProducts()
clearForm()
})

}

}


function editProduct(id){

fetch(api + "/" + id)
.then(res=>res.json())
.then(p=>{

document.getElementById("id").value = p.id
document.getElementById("name").value = p.name
document.getElementById("barcode").value = p.barcode
document.getElementById("price").value = p.price
document.getElementById("cost").value = p.costPrice
document.getElementById("stock").value = p.stockQuantity
document.getElementById("category").value = p.categoryId

})

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


function searchBarcode(){

let barcode = document.getElementById("search").value.trim()

if(barcode === ""){
loadProducts()
return
}

fetch(api + "/barcode/" + barcode)
.then(res=>res.json())
.then(p=>{

const stockColor = p.stockQuantity < 10
? 'bg-danger-subtle text-danger'
: 'bg-success-subtle text-success'

let profit = p.price - p.costPrice
let categoryName = getCategoryName(p.categoryId)

let row = `
<tr>

<td>
<div class="fw-semibold">${p.name}</div>
<small class="text-muted">ID: #${p.id}</small>
</td>

<td>
<span class="badge bg-primary">${categoryName}</span>
</td>

<td><code>${p.barcode}</code></td>

<td>${Number(p.costPrice).toLocaleString("vi-VN")} đ</td>

<td class="fw-bold">${Number(p.price).toLocaleString("vi-VN")} đ</td>

<td class="text-success fw-bold">
${Number(profit).toLocaleString("vi-VN")} đ
</td>

<td>
<span class="stock-badge ${stockColor}">
${p.stockQuantity} món
</span>
</td>

<td class="text-end">

<button class="btn btn-light btn-sm me-1" onclick="editProduct(${p.id})">
<i class="fa-solid fa-pen-to-square"></i>
</button>

<button class="btn btn-light btn-sm text-danger" onclick="deleteProduct(${p.id})">
<i class="fa-solid fa-trash"></i>
</button>

</td>

</tr>
`

document.getElementById("tableBody").innerHTML = row

// clear để quét tiếp
document.getElementById("search").value = ""
document.getElementById("search").focus()

})
.catch(()=>{
alert("Không tìm thấy sản phẩm")
})

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