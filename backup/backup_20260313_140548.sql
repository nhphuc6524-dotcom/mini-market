--
-- PostgreSQL database dump
--

\restrict rY8yClaXDGV6jynWkv6BtqnzzqDzqboCF59yuKNctdUZZBJI5dMlI3dXkkmbWG7

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
    total numeric(38,2) DEFAULT 0,
    total_amount numeric(38,2)
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

COPY public.orders (id, user_id, order_date, total, total_amount) FROM stdin;
2	\N	2026-03-12 17:12:10.71876	38000.00	\N
3	\N	2026-03-12 17:12:10.71876	25000.00	\N
4	\N	2026-03-12 17:12:10.71876	52000.00	\N
5	\N	2026-03-12 17:12:10.71876	15000.00	\N
12	\N	2026-03-12 18:18:03.072173	8500.00	\N
13	\N	2026-03-10 13:37:35.193602	42736.00	\N
14	\N	2026-03-12 16:11:57.775788	84436.00	\N
15	\N	2026-03-10 23:53:02.259463	46676.00	\N
16	\N	2026-03-03 20:29:45.127655	85188.00	\N
17	\N	2026-03-05 11:43:49.720827	57996.00	\N
18	\N	2026-03-06 03:28:13.517117	61530.00	\N
19	\N	2026-03-08 08:37:00.070994	63014.00	\N
20	\N	2026-03-04 01:29:22.281954	27468.00	\N
21	\N	2026-03-08 21:21:48.259928	79825.00	\N
22	\N	2026-03-08 12:50:49.144774	83354.00	\N
23	\N	2026-03-11 02:13:34.736252	67001.00	\N
24	\N	2026-03-06 19:53:28.937926	45426.00	\N
25	\N	2026-03-09 07:53:04.899698	23603.00	\N
26	\N	2026-03-09 16:01:50.522129	32385.00	\N
27	\N	2026-03-06 00:48:36.664695	31773.00	\N
28	\N	2026-03-11 09:21:19.429873	17383.00	\N
29	\N	2026-03-08 05:46:15.30399	82947.00	\N
30	\N	2026-03-05 02:07:11.203122	78518.00	\N
31	\N	2026-03-04 01:48:40.511457	53101.00	\N
32	\N	2026-03-11 16:48:45.272786	49270.00	\N
33	\N	2026-03-06 12:44:51.386919	57137.00	\N
34	\N	2026-03-12 05:44:37.500414	68171.00	\N
35	\N	2026-03-09 03:25:52.722446	94066.00	\N
36	\N	2026-03-07 09:46:57.951893	28967.00	\N
37	\N	2026-03-13 09:01:44.287007	76148.00	\N
38	\N	2026-03-11 07:42:58.731056	54941.00	\N
39	\N	2026-03-12 10:21:37.271257	47257.00	\N
40	\N	2026-03-09 05:29:59.671808	64950.00	\N
41	\N	2026-03-08 22:52:43.885725	65983.00	\N
42	\N	2026-03-04 11:22:21.561163	53739.00	\N
43	\N	2026-03-03 22:23:22.516576	82012.00	\N
44	\N	2026-03-08 23:08:57.045332	80975.00	\N
45	\N	2026-03-12 06:00:47.932825	25102.00	\N
46	\N	2026-03-07 17:05:03.610858	34361.00	\N
47	\N	2026-03-12 03:02:50.345999	17907.00	\N
48	\N	2026-03-11 19:02:04.035129	24160.00	\N
49	\N	2026-03-05 14:13:55.236014	85014.00	\N
50	\N	2026-03-05 12:45:52.832217	64513.00	\N
51	\N	2026-03-10 08:48:49.39921	60485.00	\N
52	\N	2026-03-10 04:07:19.847263	75730.00	\N
53	\N	2026-03-11 05:55:59.604602	88401.00	\N
54	\N	2026-03-11 05:31:58.215039	16158.00	\N
55	\N	2026-03-04 18:02:45.911392	67788.00	\N
56	\N	2026-03-08 02:16:54.068083	87562.00	\N
57	\N	2026-03-12 23:58:37.694721	60594.00	\N
58	\N	2026-03-13 06:44:09.936222	8164.00	\N
59	\N	2026-03-04 09:30:01.165081	58939.00	\N
60	\N	2026-03-06 23:33:10.404529	14779.00	\N
61	\N	2026-03-12 12:27:04.681488	11464.00	\N
62	\N	2026-03-08 17:38:03.319199	12299.00	\N
63	\N	2026-03-05 02:37:18.950509	86440.00	\N
64	\N	2026-03-08 17:58:42.791831	25691.00	\N
65	\N	2026-03-05 01:46:45.211381	68458.00	\N
66	\N	2026-03-05 03:54:35.95659	38119.00	\N
67	\N	2026-03-04 10:13:53.351134	25969.00	\N
68	\N	2026-03-10 10:47:01.551467	89198.00	\N
69	\N	2026-03-12 04:22:33.116914	67997.00	\N
70	\N	2026-03-04 17:25:12.214399	53138.00	\N
71	\N	2026-03-06 23:00:18.323293	31219.00	\N
72	\N	2026-03-10 13:44:50.405491	61971.00	\N
73	\N	2026-03-04 12:15:16.907176	95963.00	\N
74	\N	2026-03-11 21:29:15.072574	3148.00	\N
75	\N	2026-03-04 15:54:22.062105	3033.00	\N
76	\N	2026-03-08 11:59:21.824248	23370.00	\N
77	\N	2026-03-09 07:01:41.438455	14069.00	\N
78	\N	2026-03-08 15:57:23.588722	35499.00	\N
79	\N	2026-03-11 23:43:22.984008	19634.00	\N
80	\N	2026-03-08 10:45:01.070657	8693.00	\N
81	\N	2026-03-13 08:33:01.788303	72328.00	\N
82	\N	2026-03-05 14:54:30.614646	43158.00	\N
83	\N	2026-03-09 01:56:25.098529	73959.00	\N
84	\N	2026-03-06 16:46:49.423866	69068.00	\N
85	\N	2026-03-09 19:34:22.879303	69352.00	\N
86	\N	2026-03-08 13:01:08.421468	59048.00	\N
87	\N	2026-03-10 03:01:26.004922	39980.00	\N
88	\N	2026-03-09 02:33:15.119817	89276.00	\N
89	\N	2026-03-13 12:18:26.687437	19991.00	\N
90	\N	2026-03-08 06:45:08.933686	96500.00	\N
91	\N	2026-03-13 01:40:57.603119	12335.00	\N
92	\N	2026-03-11 02:42:10.430328	46625.00	\N
93	\N	2026-03-06 02:41:47.231785	2836.00	\N
94	\N	2026-03-03 17:11:58.845941	47679.00	\N
95	\N	2026-03-04 00:37:32.322475	38660.00	\N
96	\N	2026-03-07 10:05:43.482993	33853.00	\N
97	\N	2026-03-06 11:16:28.576118	41633.00	\N
98	\N	2026-03-04 04:27:11.282791	33887.00	\N
99	\N	2026-03-09 01:33:37.831661	25714.00	\N
100	\N	2026-03-09 10:29:21.993849	35791.00	\N
101	\N	2026-03-09 20:52:36.675536	3720.00	\N
102	\N	2026-03-09 19:12:21.048713	20791.00	\N
103	\N	2026-03-05 01:24:14.809677	95227.00	\N
104	\N	2026-03-11 04:42:39.712599	50847.00	\N
105	\N	2026-03-12 09:33:59.846488	92296.00	\N
106	\N	2026-03-06 01:08:23.729068	65626.00	\N
107	\N	2026-03-09 13:35:26.105302	20077.00	\N
108	\N	2026-03-10 07:38:20.618891	51047.00	\N
109	\N	2026-03-10 01:59:26.869055	90454.00	\N
110	\N	2026-03-04 01:52:36.735716	80486.00	\N
111	\N	2026-03-11 09:38:19.386125	93574.00	\N
112	\N	2026-03-06 08:38:05.550824	14633.00	\N
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

\unrestrict rY8yClaXDGV6jynWkv6BtqnzzqDzqboCF59yuKNctdUZZBJI5dMlI3dXkkmbWG7

