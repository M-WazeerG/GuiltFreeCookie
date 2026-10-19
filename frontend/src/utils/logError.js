// Details for developers in `npm run dev`; silent in production builds,
// where the toast or inline message already tells the user what happened.
export function logError(...args) {
  if (import.meta.env.DEV) console.error(...args);
}
