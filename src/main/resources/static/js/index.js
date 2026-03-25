function getCurrentUser() {
    const userData = localStorage.getItem("user");
    if(!userData) return null;
    try {
        const user = JSON.parse(userData);
        if(!user.id) return null;
        return user;
    } catch(e) {
        console.error("Lỗi parse user:", e);
        return null;
    }
}
// ---------------------------
// PHẦN 1: Quản lý giỏ hàng
// ---------------------------

const productAPI = "/api/products/barcode/";
const orderAPI = "/api/orders";
const beep = new Audio("https://actions.google.com/sounds/v1/cartoon/wood_plank_flicks.ogg");
let cart = [];
let barcodeInput;
let discount = 0;
document.addEventListener("DOMContentLoaded", () => {
    barcodeInput = document.getElementById("barcodeInput");
    barcodeInput.addEventListener("keydown", scanBarcode);
});

let scanLock = false;

function scanBarcode(e) {

    if (e.key !== "Enter") return;
    if (scanLock) return;

    scanLock = true;

    let barcode = barcodeInput.value.trim();

    if(!barcode){
        scanLock = false;
        return;
    }

    fetch(productAPI + barcode)
        .then(res => {
            if(!res.ok) throw new Error("Not found");
            return res.json();
        })
        .then(product => {

            beep.play();
            addToCart(product);
            barcodeInput.value = "";

        })
        .catch(()=>{
            alert("Không tìm thấy sản phẩm");
        })
        .finally(()=>{
            setTimeout(()=> scanLock = false , 300);
        });
}
function addToCart(product) {
    let existing = cart.find(p => p.id === product.id);
    if (existing) {
        existing.qty++;
    } else {
        cart.push({
            id: product.id,
            name: product.name,
            price: product.price,
            qty: 1,
            barcode: product.barcode || null
        });
    }
    renderCart();
    document.getElementById("barcodeInput").focus();
}

function renderCart() {

    let rows = "";
    let total = 0;
    let count = 0;

    if (cart.length > 0)
        document.getElementById("emptyCart").style.display = "none";
    else
        document.getElementById("emptyCart").style.display = "block";

    cart.forEach((p, index) => {

        let sub = p.price * p.qty;

        total += sub;
        count += p.qty;

        rows += `
        <tr>
            <td class="ps-4">
                <div class="fw-bold text-dark">${p.name}</div>
                <small class="text-muted">Barcode: ${p.barcode || 'N/A'}</small>
            </td>

            <td class="text-center fw-semibold text-secondary">
                ${p.price.toLocaleString()}
            </td>

            <td class="text-center">
                <div class="d-flex align-items-center justify-content-center gap-2">
                    <button class="btn btn-outline-secondary" onclick="decrease(${index})">-</button>
                    <span class="fw-bold">${p.qty}</span>
                    <button class="btn btn-outline-secondary" onclick="increase(${index})">+</button>
                </div>
            </td>

            <td class="text-end fw-bold text-primary">
                ${sub.toLocaleString()} đ
            </td>

            <td class="text-center pe-4">
                <button class="btn btn-link text-danger p-0" onclick="removeItem(${index})">
                    <i class="fa-solid fa-circle-xmark"></i>
                </button>
            </td>
        </tr>
        `;
    });

    // lấy discount
    let discountValue = Number(document.getElementById("discount")?.value || 0);
    let discountType = document.getElementById("discountType")?.value;

    let discount = 0;

    if(discountType === "percent"){
        discount = total * discountValue / 100;
    }else{
        discount = discountValue;
    }

    total = total - discount;

    if(total < 0) total = 0;

    document.getElementById("cartTable").innerHTML = rows;
    document.getElementById("totalPrice").innerText = total.toLocaleString();
    document.getElementById("itemCount").innerText = count;

    calculateChange();
}

// Phím tắt F8 & F9
document.addEventListener('keydown', (e) => {
    if (e.key === 'F8') clearCart();
    if (e.key === 'F9') checkout();
});

function clearCart() {
    if (confirm("Bạn có chắc muốn hủy giỏ hàng?")) {
        cart = [];
        renderCart();
    }
}

function increase(i) {
    cart[i].qty++;
    renderCart();
}

function decrease(i) {
    if (cart[i].qty > 1) {
        cart[i].qty--;
    } else {
        cart.splice(i, 1);
    }
    renderCart();
}

function removeItem(i) {
    cart.splice(i, 1);
    renderCart();
}

async function checkout() {
    if (cart.length === 0) {
        alert("Giỏ hàng trống");
        return;
    }

    const user = getCurrentUser(); // lấy user thực sự
    if (!user) {
        alert("User chưa đăng nhập hoặc thông tin user không hợp lệ");
        window.location.href = "/login.html";
        return;
    }

    // Lấy giá trị discount
    let discountValue = Number(document.getElementById("discount")?.value || 0);
    let discountType = document.getElementById("discountType")?.value;

    // Tính tổng
    let total = cart.reduce((sum, p) => sum + p.price * p.qty, 0);

    // Tính discount
    let discount = 0;
    if (discountType === "percent") {
        discount = total * discountValue / 100;
    } else {
        discount = discountValue;
    }

    total = Math.max(total - discount, 0);

    // Chuẩn bị payload, dùng user.id từ getCurrentUser()
    const payload = {
        userId: user.id,
        total: total,
        discount: discount,
        paymentMethod: document.getElementById("paymentMethod")?.value || "cash",
        items: cart.map(p => ({
            productId: p.id,
            quantity: p.qty,
            price: p.price
        }))
    };

    console.log("Checkout payload:", payload);

    // Gửi request tạo đơn hàng
    try {
        let res = await fetch(orderAPI, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const errorText = await res.text();
            alert("Không thể tạo đơn hàng: " + errorText);
            return;
        }

        let order = await res.json();
        alert(`Thanh toán thành công - Order #${order.id}`);
        exportPDF(order.id);

        // Reset giỏ hàng & form
        cart = [];
        renderCart();
        document.getElementById("cashReceived").value = "";
        document.getElementById("discount").value = 0;
        document.getElementById("changeBox").style.display = "none";
        barcodeInput.focus();

    } catch (err) {
        console.error("Checkout error:", err);
        alert("Đã xảy ra lỗi khi tạo đơn hàng.");
    }
}

// ---------------------------
// PHẦN 2: Quét barcode bằng camera
// ---------------------------

let scanner;
let cameraRunning = false;

function startScanner() {

    if (cameraRunning) return;

    document.getElementById("reader").style.display = "block";

    scanner = new Html5Qrcode("reader");

    scanner.start(
    { facingMode: "environment" },
    { 
        fps: 10,
        qrbox: { width: 300, height: 120 },
        aspectRatio: 1.7
    },
        (barcode) => {

            if (scanLock) return;
            scanLock = true;

            barcode = barcode.trim();

            beep.play();

            barcodeInput.value = barcode;

            fetch(productAPI + barcode)
                .then(res => {
                    if (!res.ok) throw new Error();
                    return res.json();
                })
                .then(product => {
                    addToCart(product);
                    barcodeInput.focus();
                })
                .catch(() => {
                    alert("Không tìm thấy sản phẩm");
                });

            barcodeInput.value = "";

            setTimeout(()=>scanLock=false,2000);
        }
    );

    cameraRunning = true;
}

function stopScanner() {

    if(scanner && cameraRunning){

        scanner.stop().then(()=>{
            document.getElementById("reader").style.display="none";
        });

        cameraRunning=false;
    }
}

// ---------------------------
// PHẦN 3: Hiển thị user & thời gian
// ---------------------------

// Hàm cập nhật user và thời gian
window.onload = function () {

    function updateUserAndTime() {

        // Lấy user
        const userData = localStorage.getItem("user");

        let username = "Guest";

        if (userData) {
            try {
                const user = JSON.parse(userData);
                username = user.username;
            } catch(e) {
                console.error("User parse error", e);
            }
        }

        document.getElementById("currentUser").innerText =
            "User: " + username;

        // Thời gian
        const now = new Date();

        const time =
            now.getHours().toString().padStart(2,"0") + ":" +
            now.getMinutes().toString().padStart(2,"0") + ":" +
            now.getSeconds().toString().padStart(2,"0");

        document.getElementById("currentTime").innerText = time;
    }

    setInterval(updateUserAndTime, 1000);

    updateUserAndTime();
};
function calculateChange(){

    let total = cart.reduce((sum,p)=>sum + p.price * p.qty,0);

    let discountValue = Number(document.getElementById("discount")?.value || 0);
    let discountType = document.getElementById("discountType")?.value;

    let discount = 0;

    if(discountType === "percent"){
        discount = total * discountValue / 100;
    }else{
        discount = discountValue;
    }

    total = total - discount;

    if(total < 0) total = 0;

    let cash = Number(document.getElementById("cashReceived").value);

    if(!cash || cash < total){
        document.getElementById("changeBox").style.display="none";
        return;
    }

    let change = cash - total;

    document.getElementById("changeBox").style.display="block";
    document.getElementById("changeAmount").innerText =
        change.toLocaleString() + " đ";
}
function exportPDF(orderId){

let itemsHTML = "";
let total = 0;

cart.forEach(p=>{

let sub = p.price * p.qty;

itemsHTML += `
<div style="display:flex; justify-content:space-between">
<span>${p.name} x${p.qty}</span>
<span>${sub.toLocaleString()}</span>
</div>
`;

total += sub;

});

document.getElementById("invoiceItems").innerHTML = itemsHTML;

document.getElementById("invoiceTotal").innerHTML = `
<div style="display:flex; justify-content:space-between">
<b>Tong</b>
<b>${total.toLocaleString()} đ</b>
</div>
`;

let element = document.getElementById("invoice");

element.style.display="block";

html2pdf()
.set({
margin:5,
filename:`hoa-don-${orderId}.pdf`,
html2canvas:{scale:2},
jsPDF:{unit:'mm',format:'a4'}
})
.from(element)
.save()
.then(()=>{
element.style.display="none";
});

}

