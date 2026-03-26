function logout() {
    localStorage.removeItem("user"); // xóa session
    window.location.href = "/login.html"; // chuyển về login
}

document.addEventListener("DOMContentLoaded", () => {
    const sidebarContainer = document.getElementById("sidebar");

    fetch("/components/sidebar.html")
    .then(res => res.text())
    .then(data => {
        sidebarContainer.innerHTML = data;

        const menu = document.getElementById("sidebarMenu");
        const user = JSON.parse(localStorage.getItem("user") || "{}");

        if (!user.role) {
            window.location.href = "/login.html";
            return;
        }

        const role = user.role;
        let html = "";

        if (role === "ADMIN") {
            html = `
                <a class="nav-link" href="/pages/index.html"><i class="fa-solid fa-cart-shopping me-2"></i> Bán hàng (POS)</a>
                <a class="nav-link" href="/pages/reports.html"><i class="fa-solid fa-chart-line me-2"></i> Dashboard</a>
                <a class="nav-link" href="/pages/orders.html"><i class="fa-solid fa-receipt me-2"></i> Hóa đơn</a>
                <a class="nav-link" href="/pages/products.html"><i class="fa-solid fa-boxes-stacked me-2"></i> Sản phẩm</a>
                <a class="nav-link" href="/pages/units.html"><i class="fa-solid fa-scale-balanced me-2"></i> Đơn vị</a> 
                <a class="nav-link" href="/pages/categories.html"><i class="fa-solid fa-layer-group me-2"></i> Danh mục</a>
                <a class="nav-link" href="/pages/suppliers.html"><i class="fa-solid fa-truck-field me-2"></i> Nhà cung cấp</a>
                <a class="nav-link" href="/pages/purchase-order.html"><i class="fa-solid fa-file-import me-2"></i> Nhập hàng</a>
                <a class="nav-link" href="/pages/stock-report.html"><i class="fa-solid fa-warehouse me-2"></i> Báo cáo tồn kho</a>
                <a class="nav-link" href="/pages/report-inventory.html"><i class="fa-solid fa-chart-column me-2"></i> Nhập - Xuất - Tồn</a>
                <a class="nav-link" href="/pages/users.html"><i class="fa-solid fa-users-gear me-2"></i> Quản lý nhân viên</a>
                <a class="nav-link" href="/pages/setting.html"><i class="fa-solid fa-gear me-2"></i> Cài đặt</a>
                <a class="nav-link text-danger" onclick="logout()"><i class="fa-solid fa-right-from-bracket me-2"></i> Logout</a>
            `;
        } else if (role === "CASHIER") {
            html = `
                <a class="nav-link" href="/pages/index.html"><i class="fa-solid fa-cart-shopping me-2"></i> Bán hàng (POS)</a>
                <a class="nav-link" href="/pages/stock-report.html"><i class="fa-solid fa-warehouse me-2"></i> Báo cáo tồn kho</a>
                <a class="nav-link" href="/pages/report-inventory.html"><i class="fa-solid fa-chart-column me-2"></i> Nhập - Xuất - Tồn</a>
                <a class="nav-link" href="/pages/reports.html"><i class="fa-solid fa-chart-line me-2"></i> Dashboard</a>
                <a class="nav-link text-danger" onclick="logout()"><i class="fa-solid fa-right-from-bracket me-2"></i> Logout</a>
            `;
        }

        menu.innerHTML = html;

        // --- PHẦN MỚI THÊM: TỰ ĐỘNG ACTIVE MENU ---
        const currentPath = window.location.pathname;
        const links = menu.querySelectorAll(".nav-link");

        links.forEach(link => {
            const href = link.getAttribute("href");
            // Kiểm tra nếu đường dẫn hiện tại kết thúc bằng href của link
            if (href && currentPath.endsWith(href)) {
                link.classList.add("active");
            }
        });
        // ------------------------------------------
    });
});