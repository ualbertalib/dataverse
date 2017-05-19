--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

ALTER TABLE ONLY public.dataversetheme DROP CONSTRAINT fk_dataversetheme_dataverse_id;
DROP INDEX public.index_dataversetheme_dataverse_id;
ALTER TABLE ONLY public.dataversetheme DROP CONSTRAINT dataversetheme_pkey;
ALTER TABLE public.dataversetheme ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE public.dataversetheme_id_seq;
DROP TABLE public.dataversetheme;
SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: dataversetheme; Type: TABLE; Schema: public; Owner: dvnapp; Tablespace: 
--

CREATE TABLE dataversetheme (
    id integer NOT NULL,
    backgroundcolor character varying(255),
    linkcolor character varying(255),
    linkurl character varying(255),
    logo character varying(255),
    logoalignment character varying(255),
    logobackgroundcolor character varying(255),
    logoformat character varying(255),
    tagline character varying(255),
    textcolor character varying(255),
    dataverse_id bigint
);


ALTER TABLE dataversetheme OWNER TO dvnapp;

--
-- Name: dataversetheme_id_seq; Type: SEQUENCE; Schema: public; Owner: dvnapp
--

CREATE SEQUENCE dataversetheme_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE dataversetheme_id_seq OWNER TO dvnapp;

--
-- Name: dataversetheme_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dvnapp
--

ALTER SEQUENCE dataversetheme_id_seq OWNED BY dataversetheme.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: dvnapp
--

ALTER TABLE ONLY dataversetheme ALTER COLUMN id SET DEFAULT nextval('dataversetheme_id_seq'::regclass);


--
-- Data for Name: dataversetheme; Type: TABLE DATA; Schema: public; Owner: dvnapp
--

INSERT INTO dataversetheme VALUES (1, '328d5e', 'ffffff', 'https://www.library.ualberta.ca/research-support/data-management', 'ua_logo.png', NULL, 'F5F5F5', 'SQUARE', 'data management @ uofa', 'c9c7c9', 1);


--
-- Name: dataversetheme_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dvnapp
--

SELECT pg_catalog.setval('dataversetheme_id_seq', 1, true);


--
-- Name: dataversetheme_pkey; Type: CONSTRAINT; Schema: public; Owner: dvnapp; Tablespace: 
--

ALTER TABLE ONLY dataversetheme
    ADD CONSTRAINT dataversetheme_pkey PRIMARY KEY (id);


--
-- Name: index_dataversetheme_dataverse_id; Type: INDEX; Schema: public; Owner: dvnapp; Tablespace: 
--

CREATE INDEX index_dataversetheme_dataverse_id ON dataversetheme USING btree (dataverse_id);


--
-- Name: fk_dataversetheme_dataverse_id; Type: FK CONSTRAINT; Schema: public; Owner: dvnapp
--

ALTER TABLE ONLY dataversetheme
    ADD CONSTRAINT fk_dataversetheme_dataverse_id FOREIGN KEY (dataverse_id) REFERENCES dvobject(id);


--
-- PostgreSQL database dump complete
--

