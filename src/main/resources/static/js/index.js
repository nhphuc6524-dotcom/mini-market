function getCurrentUser() {
    const userData = localStorage.getItem("user");
    if (!userData) return null;
    try {
        const user = JSON.parse(userData);
        // Kiểm tra id có tồn tại không
        if (!user || (!user.id && !user.userId)) {
            console.error("Dữ liệu User trong Storage thiếu ID!");
            return null;
        }
        return user;
    } catch (e) {
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
    // 1. Tìm sản phẩm trong giỏ hàng hiện tại
    let existing = cart.find(p => p.id === product.id);
    let currentQtyInCart = existing ? existing.qty : 0;

    // 2. Kiểm tra tồn kho (Giả sử API trả về trường stockQuantity)
    // Nếu product.stockQuantity không có, mặc định là 0 hoặc kiểm tra giá trị thực tế từ DB
    if (currentQtyInCart + 1 > product.stockQuantity) {
        alert(`Sản phẩm [${product.name}] đã hết hàng hoặc không đủ tồn kho! (Tồn: ${product.stockQuantity})`);
        return; // Dừng lại, không thêm vào giỏ
    }

    // 3. Nếu còn hàng thì mới xử lý thêm
    if (existing) {
        existing.qty++;
    } else {
        cart.push({
            id: product.id,
            name: product.name,
            price: product.price,
            qty: 1,
            barcode: product.barcode || null,
            stockQuantity: product.stockQuantity // Lưu lại để dùng khi bấm nút + trong giỏ
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
                    <button class="btn btn-outline-secondary btn-sm" onclick="decrease(${index})">-</button>
                    <span class="fw-bold" style="min-width: 20px">${p.qty}</span>
                    <button class="btn btn-outline-secondary btn-sm" onclick="increase(${index})">+</button>
                </div>
            </td>
            <td class="text-center">
                <span class="badge ${p.stockQuantity <= 5 ? 'bg-danger' : 'bg-info'}">
                    ${p.stockQuantity}
                </span>
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
    let p = cart[i];
    if (p.qty + 1 > p.stockQuantity) {
        alert("Không thể tăng thêm. Số lượng đạt giới hạn tồn kho!");
        return;
    }
    p.qty++;
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

    const user = getCurrentUser(); 
    if (!user) {
        alert("User chưa đăng nhập hoặc thông tin user không hợp lệ");
        window.location.href = "/login.html";
        return;
    }

    let discountValue = Number(document.getElementById("discount")?.value || 0);
    let discountType = document.getElementById("discountType")?.value;
    let totalItemsPrice = cart.reduce((sum, p) => sum + p.price * p.qty, 0);

    let discount = (discountType === "percent") ? (totalItemsPrice * discountValue / 100) : discountValue;
    let finalTotal = Math.max(totalItemsPrice - discount, 0);

    const payload = {
        userId: user.id,
        total: finalTotal,
        discount: discount,
        paymentMethod: document.getElementById("paymentMethod")?.value || "cash",
        items: cart.map(p => ({
            productId: p.id,
            quantity: p.qty,
            price: p.price
        }))
    };

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

        // Đợi xuất PDF xong rồi mới xóa dữ liệu giỏ hàng
        await exportPDF(order.id);

        // Reset sau khi đã tải PDF thành công
        cart = [];
        renderCart();
        document.getElementById("cashReceived").value = "";
        document.getElementById("discount").value = 0;
        if(document.getElementById("changeBox")) document.getElementById("changeBox").style.display = "none";
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
    // 1. Lấy thông tin User (giữ nguyên logic cũ của bạn)
    const userData = localStorage.getItem("user");
    let username = "Guest";
    if (userData) {
        try {
            const user = JSON.parse(userData);
            username = user.username;
        } catch(e) { console.error(e); }
    }
    document.getElementById("currentUser").innerText = "Thu ngân: " + username;

    // 2. Cập nhật Ngày và Giờ
    const now = new Date();
    
    // Định dạng ngày: DD/MM/YYYY
    const dateStr = now.getDate().toString().padStart(2, '0') + '/' + 
                    (now.getMonth() + 1).toString().padStart(2, '0') + '/' + 
                    now.getFullYear();

    // Định dạng giờ: HH:mm:ss
    const timeStr = now.getHours().toString().padStart(2, '0') + ":" + 
                    now.getMinutes().toString().padStart(2, '0') + ":" + 
                    now.getSeconds().toString().padStart(2, '0');

    // Hiển thị kết hợp: 26/03/2026 - 01:55:46
    document.getElementById("currentTime").innerText = `${dateStr} - ${timeStr}`;
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

function exportPDF(orderId) {
    return new Promise((resolve) => {
        const user = typeof getCurrentUser === 'function' ? getCurrentUser() : null;
        const cashierName = user ? user.username : "Guest";
        const now = new Date().toLocaleString('vi-VN');
        
        // Tính toán lại các con số
        let subTotal = cart.reduce((sum, p) => sum + (p.price * p.qty), 0);
        let discountValue = Number(document.getElementById("discount")?.value || 0);
        let discountType = document.getElementById("discountType")?.value;
        let discountAmount = (discountType === "percent") ? (subTotal * discountValue / 100) : discountValue;
        let finalTotal = Math.max(subTotal - discountAmount, 0);
        
        let cashInValue = document.getElementById("cashReceived").value;
        let cashIn = cashInValue ? Number(cashInValue) : finalTotal;
        let change = cashIn - finalTotal;

        // --- ĐIỀN DỮ LIỆU VÀO TEMPLATE ẨN ---
        // Thông tin cửa hàng
        document.getElementById("pdfStoreName").innerText = "SIÊU THỊ MINI";
        document.getElementById("pdfStoreAddress").innerText = "225 Nguyễn Thông, P. Phú Thủy, Phan Thiết, Bình Thuận";
        document.getElementById("pdfStorePhone").innerText = "0123456789";

        // Thông tin hóa đơn
        document.getElementById("pdfOrderId").innerText = orderId;
        document.getElementById("pdfCashier").innerText = cashierName;
        document.getElementById("invoiceTimeStr").innerText = now;
        
        let itemsHTML = "";
        cart.forEach(p => {
            itemsHTML += `
                <tr style="border-bottom: 1px dotted #eee;">
                    <td style="padding: 5px 0;">${p.name}</td>
                    <td style="text-align:center;">${p.qty}</td>
                    <td style="text-align:right;">${(p.price * p.qty).toLocaleString()}</td>
                </tr>`;
        });
        
        document.getElementById("pdfItemsList").innerHTML = itemsHTML;
        document.getElementById("pdfSubTotal").innerText = subTotal.toLocaleString() + " đ";
        document.getElementById("pdfDiscount").innerText = "-" + discountAmount.toLocaleString() + " đ";
        document.getElementById("pdfFinalTotal").innerText = finalTotal.toLocaleString() + " đ";
        document.getElementById("pdfCashIn").innerText = cashIn.toLocaleString() + " đ";
        document.getElementById("pdfChange").innerText = (change > 0 ? change : 0).toLocaleString() + " đ";

        const element = document.getElementById("invoice");
        element.style.display = "block"; 

        // Đợi 500ms để đảm bảo nội dung đã render đủ
        setTimeout(() => {
            const opt = {
                margin: 0,
                filename: `HD-${orderId}.pdf`,
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 2, useCORS: true, logging: false },
                jsPDF: { unit: 'mm', format: [80, 200], orientation: 'portrait' }
            };

            html2pdf().set(opt).from(element).save().then(() => {
                element.style.display = "none";
                resolve(); 
            }).catch((err) => {
                console.error("PDF Export Error:", err);
                element.style.display = "none";
                resolve();
            });
        }, 500);
    });
}