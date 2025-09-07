CREATE TABLE IF NOT EXISTS books (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  title TEXT NOT NULL,
  author TEXT NOT NULL,
  pages INTEGER NOT NULL CHECK (pages > 0),
  price REAL,
  published_date TEXT, -- ISO yyyy-MM-dd
  description TEXT,
  created_at TEXT DEFAULT (datetime('now'))
);
