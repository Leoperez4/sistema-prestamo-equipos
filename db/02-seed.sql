-- ============================================================
-- Datos ficticios para desarrollo y demo.
-- Ningún dato real, según las directrices de seguridad del reto.
-- ============================================================

INSERT INTO usuarios (nombre, email) VALUES
  ('Ana Torres',       'ana.torres@empresa-demo.local'),
  ('Luis Ramirez',     'luis.ramirez@empresa-demo.local'),
  ('Sofia Pardo',      'sofia.pardo@empresa-demo.local');

INSERT INTO equipos (nombre, tipo, numero_serie, estado) VALUES
  ('Dell Latitude 5440',      'LAPTOP',     'SN-LAP-0001', 'DISPONIBLE'),
  ('MacBook Air M2',          'LAPTOP',     'SN-LAP-0002', 'DISPONIBLE'),
  ('Lenovo ThinkPad T14',     'LAPTOP',     'SN-LAP-0003', 'DISPONIBLE'),
  ('Monitor LG 27 UltraFine', 'MONITOR',    'SN-MON-0001', 'DISPONIBLE'),
  ('Monitor Dell P2422H',     'MONITOR',    'SN-MON-0002', 'DISPONIBLE'),
  ('iPad Air 11',             'TABLET',     'SN-TAB-0001', 'DISPONIBLE'),
  ('Samsung Galaxy Tab S9',   'TABLET',     'SN-TAB-0002', 'DISPONIBLE'),
  ('Epson PowerLite 1795F',   'PROYECTOR',  'SN-PRO-0001', 'MANTENIMIENTO');