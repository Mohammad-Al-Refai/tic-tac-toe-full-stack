CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS players;

CREATE TABLE IF NOT EXISTS players (
  id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  sessionId VARCHAR(255) NOT NULL,
  isActive Boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS games (
  id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
  admin_id UUID NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  player_id1 UUID,
  player_id2 UUID,
  player1_name VARCHAR(255),
  player2_name VARCHAR(255),
  player_id_turn UUID,
  isPrivate Boolean DEFAULT false,
  isDone Boolean DEFAULT false,
  current_cell_type VARCHAR(5) DEFAULT 'NONE',
  board TEXT[][],
  name VARCHAR(255),
  FOREIGN KEY (admin_id) REFERENCES players(id)
);