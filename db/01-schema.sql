CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TYPE estado_equipo  AS ENUM ('DISPONIBLE', 'EN_PRESTAMO', 'MANTENIMIENTO');
CREATE TYPE estado_reserva AS ENUM ('PENDIENTE', 'APROBADA', 'DEVUELTA');

CREATE TABLE usuarios (
  id          SERIAL       PRIMARY KEY,
  nombre      VARCHAR(120) NOT NULL,
  email       VARCHAR(160) NOT NULL UNIQUE,
  created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE equipos (
  id            SERIAL        PRIMARY KEY,
  nombre        VARCHAR(120)  NOT NULL,
  tipo          VARCHAR(60)   NOT NULL,
  numero_serie  VARCHAR(80)   NOT NULL UNIQUE,
  estado        estado_equipo NOT NULL DEFAULT 'DISPONIBLE',
  created_at    TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE TABLE reservas (
  id            SERIAL         PRIMARY KEY,
  usuario_id    INTEGER        NOT NULL REFERENCES usuarios(id),
  equipo_id     INTEGER        NOT NULL REFERENCES equipos(id),
  fecha_inicio  DATE           NOT NULL,
  fecha_fin     DATE           NOT NULL,
  estado        estado_reserva NOT NULL DEFAULT 'PENDIENTE',
  created_at    TIMESTAMPTZ    NOT NULL DEFAULT now(),

  CONSTRAINT fechas_validas
    CHECK (fecha_fin >= fecha_inicio),
  
  CONSTRAINT sin_solapamiento EXCLUDE USING gist (
    equipo_id WITH =,
    daterange(fecha_inicio, fecha_fin, '[]') WITH &&
  ) WHERE (estado <> 'DEVUELTA')
);

CREATE INDEX idx_reservas_equipo_fecha
  ON reservas (equipo_id, fecha_inicio);

  
