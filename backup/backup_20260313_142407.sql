--
-- PostgreSQL database dump
--

\restrict ziyGNorRgk4u4DE0PzZg6w6DRJANnqZb6kCs91eC1eQrNacNyfd4hu9F9vdPrly

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: order_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_items (
    id bigint NOT NULL,
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    quantity integer NOT NULL,
    price numeric(38,2) NOT NULL
);


ALTER TABLE public.order_items OWNER TO postgres;

--
-- Name: order_items_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.order_items_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.order_items_id_seq OWNER TO postgres;

--
-- Name: order_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.order_items_id_seq OWNED BY public.order_items.id;


--
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    id bigint NOT NULL,
    user_id integer,
    order_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    total numeric(38,2) DEFAULT 0
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.orders_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.orders_id_seq OWNER TO postgres;

--
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    barcode character varying(255),
    price numeric(38,2) NOT NULL,
    stock_quantity integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    category_id integer
);


ALTER TABLE public.products OWNER TO postgres;

--
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.products_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.products_id_seq OWNER TO postgres;

--
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- Name: settings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.settings (
    id integer NOT NULL,
    store_name character varying(255),
    store_address character varying(255),
    store_phone character varying(255),
    receipt_footer character varying(255),
    receipt_paper_size character varying(255) DEFAULT '80mm'::character varying,
    low_stock_alert integer DEFAULT 10,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.settings OWNER TO postgres;

--
-- Name: settings_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.settings_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.settings_id_seq OWNER TO postgres;

--
-- Name: settings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.settings_id_seq OWNED BY public.settings.id;


--
-- Name: order_items id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_items ALTER COLUMN id SET DEFAULT nextval('public.order_items_id_seq'::regclass);


--
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- Name: products id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- Name: settings id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.settings ALTER COLUMN id SET DEFAULT nextval('public.settings_id_seq'::regclass);


--
-- Data for Name: order_items; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.order_items (id, order_id, product_id, quantity, price) FROM stdin;
4	2	11	1	15000.00
5	2	3	1	8500.00
6	3	8	5	3500.00
7	3	6	2	7000.00
8	4	24	1	10000.00
9	4	29	1	5000.00
10	5	23	2	35000.00
12	12	4	1	8500.00
\.


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orders (id, user_id, order_date, total) FROM stdin;
2	\N	2026-03-12 17:12:10.71876	38000.00
3	\N	2026-03-12 17:12:10.71876	25000.00
4	\N	2026-03-12 17:12:10.71876	52000.00
5	\N	2026-03-12 17:12:10.71876	15000.00
12	\N	2026-03-12 18:18:03.072173	8500.00
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.products (id, name, barcode, price, stock_quantity, created_at, category_id) FROM stdin;
4	7Up Lon 330ml	8934588013070	8500.00	90	2026-03-12 17:12:06.382599	\N
5	Sting Đỏ	8934588013087	11000.00	70	2026-03-12 17:12:06.382599	\N
6	Red Bull	8934588013094	12000.00	80	2026-03-12 17:12:06.382599	\N
7	Aquafina 500ml	8935049500017	7000.00	150	2026-03-12 17:12:06.382599	\N
8	Dasani 500ml	8935049500024	6500.00	130	2026-03-12 17:12:06.382599	\N
9	Mì Hảo Hảo Tôm Chua Cay	8934563131168	3500.00	200	2026-03-12 17:12:06.382599	\N
10	Mì Omachi Sườn	8934563131175	7000.00	180	2026-03-12 17:12:06.382599	\N
11	Mì Gấu Đỏ	8934563131182	4000.00	160	2026-03-12 17:12:06.382599	\N
12	Bánh Oreo	8934680020012	15000.00	60	2026-03-12 17:12:06.382599	\N
13	Bánh ChocoPie	8934680020029	25000.00	50	2026-03-12 17:12:06.382599	\N
14	Bánh AFC	8934680020036	18000.00	70	2026-03-12 17:12:06.382599	\N
15	Snack Oishi Tôm	8934804010011	8000.00	90	2026-03-12 17:12:06.382599	\N
16	Snack Poca BBQ	8934804010028	10000.00	85	2026-03-12 17:12:06.382599	\N
17	Sữa Vinamilk 180ml	8935217400012	6500.00	140	2026-03-12 17:12:06.382599	\N
18	Sữa TH True Milk	8935217400029	7000.00	120	2026-03-12 17:12:06.382599	\N
19	Cà phê G7 3in1	8935250700013	45000.00	40	2026-03-12 17:12:06.382599	\N
20	Cà phê Nescafe	8935250700020	42000.00	45	2026-03-12 17:12:06.382599	\N
21	Đường Biên Hòa 1kg	8935301200011	28000.00	50	2026-03-12 17:12:06.382599	\N
22	Muối Iốt	8935301200028	6000.00	60	2026-03-12 17:12:06.382599	\N
23	Dầu ăn Neptune 1L	8935301200035	42000.00	55	2026-03-12 17:12:06.382599	\N
24	Nước mắm Nam Ngư	8935301200042	35000.00	65	2026-03-12 17:12:06.382599	\N
25	Trà xanh C2	8935311200014	10000.00	100	2026-03-12 17:12:06.382599	\N
26	Trà Ô Long Tea+	8935311200021	11000.00	90	2026-03-12 17:12:06.382599	\N
27	Bánh mì Sandwich	8935321200015	20000.00	40	2026-03-12 17:12:06.382599	\N
28	Xúc xích Đức Việt	8935321200022	25000.00	35	2026-03-12 17:12:06.382599	\N
29	Kẹo Mentos	8935331200016	12000.00	70	2026-03-12 17:12:06.382599	\N
30	Kẹo Alpenliebe	8935331200023	15000.00	60	2026-03-12 17:12:06.382599	\N
31	Bột giặt Omo 800g	8935341200017	42000.00	30	2026-03-12 17:12:06.382599	\N
3	Pepsi 330ml	8939876543210	9000.00	80	2026-03-12 17:06:45.734603	\N
1	Coca Cola 330ml	8935049501012	10000.00	98	2026-03-07 15:46:47.438242	\N
\.


--
-- Data for Name: settings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.settings (id, store_name, store_address, store_phone, receipt_footer, receipt_paper_size, low_stock_alert, created_at) FROM stdin;
1	Siêu thị Mini	225 Nguyễn Thông, Phường Phú Thủy, Tỉnh Lâm Đồng	0123456789		80mm	10	2026-03-13 13:55:34.225378
\.


--
-- Name: order_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.order_items_id_seq', 12, true);


--
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orders_id_seq', 112, true);


--
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.products_id_seq', 32, true);


--
-- Name: settings_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.settings_id_seq', 1, true);


--
-- Name: order_items order_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_pkey PRIMARY KEY (id);


--
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- Name: products products_barcode_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_barcode_key UNIQUE (barcode);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: settings settings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.settings
    ADD CONSTRAINT settings_pkey PRIMARY KEY (id);


--
-- Name: order_items fk_order; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- Name: order_items fk_product; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- PostgreSQL database dump complete
--

\unrestrict ziyGNorRgk4u4DE0PzZg6w6DRJANnqZb6kCs91eC1eQrNacNyfd4hu9F9vdPrly

