Mình sẽ **viết lại nội dung database thành dạng tài liệu `.txt` dễ đọc** (không còn dump của PostgreSQL).
Bạn có thể lưu file này thành:

```
database_structure.txt
```

hoặc

```
README_DATABASE.txt
```

để người khác hiểu nhanh cấu trúc database.

---

# MINI SUPERMARKET DATABASE STRUCTURE

Database: `minimarket`
Purpose: POS system for Mini Supermarket

---

# 1. TABLE: categories

Danh mục sản phẩm.

Fields:

```
id            INTEGER PRIMARY KEY
name          VARCHAR(100) NOT NULL
description   TEXT
```

Example:

```
1  Nước uống
2  Mì gói
3  Bánh kẹo
4  Gia vị
5  Đồ gia dụng
```

---

# 2. TABLE: units

Đơn vị tính sản phẩm.

Fields:

```
id      INTEGER PRIMARY KEY
name    VARCHAR(50)
```

Example:

```
1  Lon
2  Chai
3  Gói
4  Hộp
5  Kg
```

---

# 3. TABLE: products

Danh sách sản phẩm bán trong siêu thị.

Fields:

```
id              BIGINT PRIMARY KEY
name            VARCHAR(255) NOT NULL
barcode         VARCHAR(255) UNIQUE
price           NUMERIC(38,2)  (giá bán)
cost_price      NUMERIC(10,2)  (giá nhập)
stock_quantity  INTEGER        (tồn kho)
min_stock       INTEGER        (mức cảnh báo tồn kho)
category_id     INTEGER        -> categories.id
unit_id         INTEGER        -> units.id
created_at      TIMESTAMP
```

Example product:

```
Coca Cola 330ml
Barcode: 8935049501012
Price: 10000
Stock: 98
```

---

# 4. TABLE: orders

Hóa đơn bán hàng.

Fields:

```
id              BIGINT PRIMARY KEY
user_id         INTEGER
order_date      TIMESTAMP
subtotal        NUMERIC(12,2)
discount        NUMERIC(12,2)
total           NUMERIC(38,2)
payment_method  VARCHAR(50)
status          VARCHAR(30)
```

Example:

```
Order ID: 116
Total: 62000
Status: COMPLETED
```

---

# 5. TABLE: order_items

Chi tiết sản phẩm trong hóa đơn.

Fields:

```
id          BIGINT PRIMARY KEY
order_id    BIGINT -> orders.id
product_id  BIGINT -> products.id
quantity    INTEGER
price       NUMERIC(38,2)
subtotal    NUMERIC(12,2)
```

Example:

```
Order 113
Coca Cola x2
Price 10000
Subtotal 20000
```

---

# 6. TABLE: payments

Thông tin thanh toán.

Fields:

```
id              INTEGER PRIMARY KEY
order_id        BIGINT -> orders.id
payment_method  VARCHAR(50)
amount          NUMERIC(12,2)
paid_at         TIMESTAMP
```

Example payment methods:

```
CASH
CARD
QR
```

---

# 7. TABLE: suppliers

Nhà cung cấp sản phẩm.

Fields:

```
id          INTEGER PRIMARY KEY
name        VARCHAR(150)
phone       VARCHAR(20)
email       VARCHAR(100)
address     TEXT
created_at  TIMESTAMP
```

---

# 8. TABLE: purchase_orders

Đơn nhập hàng từ nhà cung cấp.

Fields:

```
id           INTEGER PRIMARY KEY
supplier_id  INTEGER -> suppliers.id
order_date   TIMESTAMP
total        NUMERIC(12,2)
```

---

# 9. TABLE: purchase_order_items

Chi tiết sản phẩm nhập.

Fields:

```
id                  INTEGER PRIMARY KEY
purchase_order_id   INTEGER -> purchase_orders.id
product_id          BIGINT -> products.id
quantity            INTEGER
cost_price          NUMERIC(10,2)
```

---

# 10. TABLE: returns

Hóa đơn trả hàng.

Fields:

```
id            INTEGER PRIMARY KEY
order_id      BIGINT -> orders.id
return_date   TIMESTAMP
total_refund  NUMERIC(12,2)
```

---

# 11. TABLE: return_items

Chi tiết sản phẩm trả.

Fields:

```
id             INTEGER PRIMARY KEY
return_id      INTEGER -> returns.id
product_id     BIGINT -> products.id
quantity       INTEGER
refund_amount  NUMERIC(12,2)
```

---

# 12. TABLE: users

Tài khoản hệ thống.

Fields:

```
id          INTEGER PRIMARY KEY
username    VARCHAR(50) UNIQUE
password    VARCHAR(255)
role        VARCHAR(50)
created_at  TIMESTAMP
```

Possible roles:

```
ADMIN
CASHIER
WAREHOUSE
```

---

# 13. TABLE: settings

Thông tin cửa hàng.

Fields:

```
id                  INTEGER PRIMARY KEY
store_name          VARCHAR(255)
store_address       VARCHAR(255)
store_phone         VARCHAR(255)
receipt_footer      VARCHAR(255)
receipt_paper_size  VARCHAR(255)
low_stock_alert     INTEGER
created_at          TIMESTAMP
```

Example:

```
Store: Siêu thị Mini
Address: 225 Nguyễn Thông
Phone: 0123456789
```

---

# DATABASE RELATIONSHIP

```
categories
     │
     │
products ─── units
     │
     │
order_items ─── orders ─── payments

products
     │
purchase_order_items ─── purchase_orders ─── suppliers

products
     │
return_items ─── returns

users
settings
```

---

# MAIN FEATURES SUPPORTED

```
✔ Product management
✔ Barcode scanning
✔ Inventory tracking
✔ Sales POS
✔ Multiple items per order
✔ Discount support
✔ Payment tracking
✔ Supplier management
✔ Purchase orders
✔ Return orders
✔ Stock alerts
✔ Sales reports
```

---

Nếu bạn muốn, mình có thể **viết thêm 1 file README chuẩn GitHub cho project này** (rất quan trọng cho portfolio), gồm:

* System architecture
* API design
* Database ERD
* How to run project

Để project **trông giống production system ~90%**.
