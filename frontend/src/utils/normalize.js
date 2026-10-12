/**
 * normalize — accept messy input, send clean data (Postel's law).
 * Used at every form boundary before validating or calling the API.
 */

// "  Sarah@Example.CO.ZA " -> "sarah@example.co.za"
export const cleanEmail = (value) => String(value ?? "").trim().toLowerCase();

// Trim and collapse runs of whitespace: "  Choc   chip \n" -> "Choc chip"
export const cleanText = (value) => String(value ?? "").replace(/\s+/g, " ").trim();

// Multi-line text keeps its line breaks, just trims each line's edges.
export const cleanMultiline = (value) =>
  String(value ?? "")
    .split("\n")
    .map((line) => line.trim())
    .join("\n")
    .replace(/\n{3,}/g, "\n\n")
    .trim();

// "oats ,almond flour;;  butter\ncinnamon," -> "oats, almond flour, butter, cinnamon"
export const cleanList = (value) =>
  String(value ?? "")
    .split(/[,;\n]+/)
    .map((item) => cleanText(item))
    .filter(Boolean)
    .join(", ");

// "R 39,99" / "39.99" / 39.99 -> 39.99
export const cleanPrice = (value) => {
  const n = Number(String(value ?? "").replace(/[^\d.,-]/g, "").replace(",", "."));
  return Number.isFinite(n) ? Math.round(n * 100) / 100 : 0;
};

export const isEmail = (value) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(cleanEmail(value));
