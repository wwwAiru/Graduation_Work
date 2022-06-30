--
-- PostgreSQL database dump
--

-- Dumped from database version 10.20
-- Dumped by pg_dump version 10.20

-- Started on 2022-06-30 07:41:33

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 205 (class 1259 OID 18693)
-- Name: accounts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.accounts (
    id bigint NOT NULL,
    account_number character varying(255),
    currency character varying(255),
    balance numeric(19,2),
    client_description character varying(255),
    client_id bigint
);


ALTER TABLE public.accounts OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 18660)
-- Name: client_invest_products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client_invest_products (
    id bigint NOT NULL,
    begin_date timestamp without time zone,
    expire_date timestamp without time zone,
    balance numeric(19,2),
    invest_prod_id bigint,
    days integer,
    profit numeric(19,2),
    account_id bigint
);


ALTER TABLE public.client_invest_products OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 18658)
-- Name: client_invest_products_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.client_invest_products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.client_invest_products_id_seq OWNER TO postgres;

--
-- TOC entry 2862 (class 0 OID 0)
-- Dependencies: 197
-- Name: client_invest_products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.client_invest_products_id_seq OWNED BY public.client_invest_products.id;


--
-- TOC entry 200 (class 1259 OID 18668)
-- Name: client_transaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client_transaction (
    id bigint NOT NULL,
    amount numeric(19,2),
    card_number character varying(255),
    client_id bigint,
    date timestamp without time zone,
    owner_name character varying(255),
    transaction_type character varying(255)
);


ALTER TABLE public.client_transaction OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 18666)
-- Name: client_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.client_transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.client_transaction_id_seq OWNER TO postgres;

--
-- TOC entry 2863 (class 0 OID 0)
-- Dependencies: 199
-- Name: client_transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.client_transaction_id_seq OWNED BY public.client_transaction.id;


--
-- TOC entry 202 (class 1259 OID 18679)
-- Name: clients; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.clients (
    id bigint NOT NULL,
    active boolean,
    balance numeric(19,2) NOT NULL,
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    middle_name character varying(255),
    password character varying(255)
);


ALTER TABLE public.clients OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 18677)
-- Name: clients_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.clients_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.clients_id_seq OWNER TO postgres;

--
-- TOC entry 2864 (class 0 OID 0)
-- Dependencies: 201
-- Name: clients_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.clients_id_seq OWNED BY public.clients.id;


--
-- TOC entry 203 (class 1259 OID 18688)
-- Name: clients_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.clients_roles (
    client_id bigint NOT NULL,
    roles character varying(255)
);


ALTER TABLE public.clients_roles OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 18691)
-- Name: deposit_accounts_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.deposit_accounts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.deposit_accounts_id_seq OWNER TO postgres;

--
-- TOC entry 2865 (class 0 OID 0)
-- Dependencies: 204
-- Name: deposit_accounts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.deposit_accounts_id_seq OWNED BY public.accounts.id;


--
-- TOC entry 196 (class 1259 OID 18592)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 18704)
-- Name: invest_products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invest_products (
    id bigint NOT NULL,
    min_deposit_term bigint,
    description text NOT NULL,
    interest_rate numeric(19,2),
    is_active boolean,
    max_deposit numeric(19,2),
    min_deposit numeric(19,2),
    name character varying(255),
    currency character varying(255),
    max_deposit_term bigint
);


ALTER TABLE public.invest_products OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 18702)
-- Name: invest_products_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.invest_products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.invest_products_id_seq OWNER TO postgres;

--
-- TOC entry 2866 (class 0 OID 0)
-- Dependencies: 206
-- Name: invest_products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.invest_products_id_seq OWNED BY public.invest_products.id;


--
-- TOC entry 2707 (class 2604 OID 18696)
-- Name: accounts id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounts ALTER COLUMN id SET DEFAULT nextval('public.deposit_accounts_id_seq'::regclass);


--
-- TOC entry 2704 (class 2604 OID 18663)
-- Name: client_invest_products id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_invest_products ALTER COLUMN id SET DEFAULT nextval('public.client_invest_products_id_seq'::regclass);


--
-- TOC entry 2705 (class 2604 OID 18671)
-- Name: client_transaction id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_transaction ALTER COLUMN id SET DEFAULT nextval('public.client_transaction_id_seq'::regclass);


--
-- TOC entry 2706 (class 2604 OID 18682)
-- Name: clients id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clients ALTER COLUMN id SET DEFAULT nextval('public.clients_id_seq'::regclass);


--
-- TOC entry 2708 (class 2604 OID 18707)
-- Name: invest_products id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invest_products ALTER COLUMN id SET DEFAULT nextval('public.invest_products_id_seq'::regclass);


--
-- TOC entry 2853 (class 0 OID 18693)
-- Dependencies: 205
-- Data for Name: accounts; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.accounts VALUES (9, '4087772240606850', 'USD', 407.07, 'Открыл счёт для покупки квартиры', 2);
INSERT INTO public.accounts VALUES (3, '4087771788900253', 'RUB', 5095000.00, '', 1);
INSERT INTO public.accounts VALUES (4, '4087771704371053', 'USD', 1039.89, '', 1);
INSERT INTO public.accounts VALUES (6, '4087771796120346', 'EUR', 1346.00, '', 1);
INSERT INTO public.accounts VALUES (10, '4087772893046305', 'EUR', 0.00, 'Инвестиции на пенсию', 2);
INSERT INTO public.accounts VALUES (8, '4087772175100901', 'RUB', 0.00, '', 2);


--
-- TOC entry 2846 (class 0 OID 18660)
-- Dependencies: 198
-- Data for Name: client_invest_products; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.client_invest_products VALUES (15, '2022-06-27 11:04:57.662359', '2022-12-26 11:04:57.662359', 200000.00, 2, 182, 10969.86, 8);
INSERT INTO public.client_invest_products VALUES (16, '2022-06-27 11:12:59.212724', '2023-06-22 11:12:59.212724', 4000.00, 16, 360, 157.81, 9);
INSERT INTO public.client_invest_products VALUES (18, '2022-06-28 19:50:14.986886', '2023-06-10 19:50:14.986886', 1000.00, 16, 347, 38.03, 4);
INSERT INTO public.client_invest_products VALUES (10, '2022-06-24 13:25:21.590757', '2023-06-24 13:25:21.590757', 918000.00, 1, 365, 73440.00, 3);
INSERT INTO public.client_invest_products VALUES (7, '2022-06-23 15:34:49.363966', '2022-12-22 15:34:49.363966', 100000.00, 2, 182, 5484.93, 3);
INSERT INTO public.client_invest_products VALUES (20, '2022-06-28 21:51:57.454014', '2022-12-28 21:51:57.454014', 1854.00, 3, 183, 37.18, 6);
INSERT INTO public.client_invest_products VALUES (21, '2022-06-29 14:40:48.737633', '2023-04-25 14:40:48.737633', 3573.73, 3, 300, 117.49, 10);
INSERT INTO public.client_invest_products VALUES (22, '2022-06-29 14:42:08.239952', '2022-12-28 14:42:08.239952', 64505.00, 2, 182, 3538.06, 8);


--
-- TOC entry 2848 (class 0 OID 18668)
-- Dependencies: 200
-- Data for Name: client_transaction; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.client_transaction VALUES (1, 1000000.00, '4815 2624 3214 6574', 1, '2022-06-14 21:51:12.748478', 'ANDREI GOLIKOV', 'Пополнение баланса');
INSERT INTO public.client_transaction VALUES (5, 67.00, '****', 1, '2022-06-24 09:01:36.936564', '****', 'Пополнение баланса');
INSERT INTO public.client_transaction VALUES (7, 50.00, '****', 1, '2022-06-26 00:40:32.777772', '****', 'Пополнение баланса');
INSERT INTO public.client_transaction VALUES (8, 50.00, '****', 1, '2022-06-26 00:47:45.218194', '****', 'Пополнение баланса');
INSERT INTO public.client_transaction VALUES (10, 50.00, '****', 1, '2022-06-26 00:49:08.696718', '****', 'Пополнение баланса');
INSERT INTO public.client_transaction VALUES (11, 50.00, '****', 1, '2022-06-26 02:04:26.822757', '****', 'Пополнение баланса');
INSERT INTO public.client_transaction VALUES (12, 50.00, '****', 1, '2022-06-26 10:20:36.779358', '****', 'Пополнение баланса');
INSERT INTO public.client_transaction VALUES (13, 575000.00, '4817 2172 5745 6687', 2, '2022-06-27 10:56:01.426025', 'DMITRIY EROFEEV', 'Пополнение баланса');
INSERT INTO public.client_transaction VALUES (14, 124587.00, '****', 2, '2022-06-27 11:06:25.36565', 'DMITRIY EROFEEV', 'Пополнение баланса');
INSERT INTO public.client_transaction VALUES (9, 50.00, '*****', 1, '2022-06-26 00:48:13.89018', 'ANDREI GOLIKOV', 'Пополнение баланса');
INSERT INTO public.client_transaction VALUES (3, 100.00, '****', 1, '2022-06-19 16:01:11.219605', 'ANDREI GOLIKOV', 'Пополнение баланса');
INSERT INTO public.client_transaction VALUES (2, 10000000.00, '4815 2624 3214 6574', 1, '2022-06-16 02:59:56.953571', 'ANDREI GOLIKOV', 'Пополнение баланса');
INSERT INTO public.client_transaction VALUES (15, 9100.00, '4815 1564 1237 4567 ', 1, '2022-06-28 19:19:48.810595', 'ANDREI GOLIKOV', 'Вывод средств');


--
-- TOC entry 2850 (class 0 OID 18679)
-- Dependencies: 202
-- Data for Name: clients; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.clients VALUES (1, true, 3550599.67, 'z-a-t-o@yandex.ru', 'Андрей', 'Голиков', 'Игоревич', '$2a$08$GHm.4xrpEGncWXVnitcN0OM0kEwWbA7DgBbwgx.SFZNNri8SDZZZG');
INSERT INTO public.clients VALUES (2, true, 0.29, 'erofeev@ya.ru', 'Дмитрий', 'Ерофеев', 'Анатольевич', '$2a$08$GHm.4xrpEGncWXVnitcN0OM0kEwWbA7DgBbwgx.SFZNNri8SDZZZG');


--
-- TOC entry 2851 (class 0 OID 18688)
-- Dependencies: 203
-- Data for Name: clients_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.clients_roles VALUES (2, 'USER');
INSERT INTO public.clients_roles VALUES (1, 'HEAD_MANAGER');


--
-- TOC entry 2855 (class 0 OID 18704)
-- Dependencies: 207
-- Data for Name: invest_products; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.invest_products VALUES (16, 182, 'Вклад со ставкой 4% годовых в долларах США, минимальный срок вклада 6 месяцев, максимальный срок 1 год. Подойдёт тем, кто опасается девальвации рубля.', 4.00, true, 100000.00, 1000.00, 'Вклад "Фантастическая четвёрка"', 'USD', 364);
INSERT INTO public.invest_products VALUES (9, 123, 'Тестовый вклад для проверки различных функций и валидации', 100.00, false, 123.00, 123.00, '"Тестовый вклад 1"', 'RUB', 1233);
INSERT INTO public.invest_products VALUES (22, 123, 'Тестовый вклад для проверки различных функций и валидации', 321.00, false, 123.00, 123.00, '"Тестовый вклад 3"', 'RUB', 1233);
INSERT INTO public.invest_products VALUES (21, 123, 'Тестовый вклад для проверки различных функций и валидации', 321.00, false, 123.00, 123.00, '"Тестовый вклад 2"', 'RUB', 1233);
INSERT INTO public.invest_products VALUES (2, 91, 'Рублёвый вклад со ставкой 11% годовых. Срок вклада от 3-х до 6 месяцев. Хорошее предложение для тех, кто хочет сберечь средства в непростой период. Минимальная сумма вклада 50 000, максимальная сумма 1 000 000 рублей.', 11.00, true, 1000000.00, 50000.00, 'Вклад "антикризисный".', 'RUB', 182);
INSERT INTO public.invest_products VALUES (3, 182, 'Вклад со ставкой 4% годовых в Евро.  Срок вклада от 6 месяцев до 1 года. Минимальная сумма вклада 1000, максимальная сумма вклада 100 000 евро. ', 4.00, true, 100000.00, 1000.00, 'Вклад "Европейская четвёрочка".', 'EUR', 364);
INSERT INTO public.invest_products VALUES (1, 364, 'Если вы откладываете на пенсию, тогда вам хорошо подойдёт вклад "Пенсионный". Вклад с минимальной суммой - 100 000 рублей, а максимальной - 10 000 000 рублей. Этот вклад учитывает среднюю долгосрочную инфляцию в России 8%, таким образом, вы отлично сохраните свои деньги до пенсии.', 8.00, true, 10000000.00, 100000.00, 'Вклад "пенсионный".', 'RUB', 1820);


--
-- TOC entry 2867 (class 0 OID 0)
-- Dependencies: 197
-- Name: client_invest_products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.client_invest_products_id_seq', 22, true);


--
-- TOC entry 2868 (class 0 OID 0)
-- Dependencies: 199
-- Name: client_transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.client_transaction_id_seq', 15, true);


--
-- TOC entry 2869 (class 0 OID 0)
-- Dependencies: 201
-- Name: clients_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.clients_id_seq', 4, true);


--
-- TOC entry 2870 (class 0 OID 0)
-- Dependencies: 204
-- Name: deposit_accounts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.deposit_accounts_id_seq', 10, true);


--
-- TOC entry 2871 (class 0 OID 0)
-- Dependencies: 196
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hibernate_sequence', 1, true);


--
-- TOC entry 2872 (class 0 OID 0)
-- Dependencies: 206
-- Name: invest_products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.invest_products_id_seq', 23, true);


--
-- TOC entry 2716 (class 2606 OID 18701)
-- Name: accounts accounts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounts
    ADD CONSTRAINT accounts_pkey PRIMARY KEY (id);


--
-- TOC entry 2710 (class 2606 OID 18665)
-- Name: client_invest_products client_invest_products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_invest_products
    ADD CONSTRAINT client_invest_products_pkey PRIMARY KEY (id);


--
-- TOC entry 2712 (class 2606 OID 18676)
-- Name: client_transaction client_transaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_transaction
    ADD CONSTRAINT client_transaction_pkey PRIMARY KEY (id);


--
-- TOC entry 2714 (class 2606 OID 18687)
-- Name: clients clients_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (id);


--
-- TOC entry 2718 (class 2606 OID 18712)
-- Name: invest_products invest_products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invest_products
    ADD CONSTRAINT invest_products_pkey PRIMARY KEY (id);


--
-- TOC entry 2720 (class 2606 OID 18734)
-- Name: client_invest_products fk4dsdv9fd7xnr5okf9ragehip3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_invest_products
    ADD CONSTRAINT fk4dsdv9fd7xnr5okf9ragehip3 FOREIGN KEY (account_id) REFERENCES public.accounts(id);


--
-- TOC entry 2719 (class 2606 OID 18718)
-- Name: client_invest_products fk6mw3x7jgq4s6i1kpw2kn7vl7w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_invest_products
    ADD CONSTRAINT fk6mw3x7jgq4s6i1kpw2kn7vl7w FOREIGN KEY (invest_prod_id) REFERENCES public.invest_products(id);


--
-- TOC entry 2721 (class 2606 OID 18723)
-- Name: clients_roles fkdfc8skx88ssraasvuujbk3kex; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clients_roles
    ADD CONSTRAINT fkdfc8skx88ssraasvuujbk3kex FOREIGN KEY (client_id) REFERENCES public.clients(id);


--
-- TOC entry 2722 (class 2606 OID 18728)
-- Name: accounts fkku1bil61rtyuvyit539yytmwl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounts
    ADD CONSTRAINT fkku1bil61rtyuvyit539yytmwl FOREIGN KEY (client_id) REFERENCES public.clients(id);


-- Completed on 2022-06-30 07:41:34

--
-- PostgreSQL database dump complete
--

