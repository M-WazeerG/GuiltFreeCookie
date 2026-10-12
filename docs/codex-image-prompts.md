# Codex image-generation prompts (Higgsfield MCP)

Paste these into Codex in VS Code, in order. Paste prompt 1 first and wait until Codex
replies with its tool inventory, credit balance and cost estimate. Then paste prompt 2.

Codex reads the Higgsfield server from `~/.codex/config.toml` (`[mcp_servers.higgsfield]`).
The CLI, the desktop app and the VS Code extension all share that file.

---

## Prompt 1: Master instruction (paste once per Codex session)

```text
You are managing product image generation for my cookie website, Guilt Free Cookie.
Project root: this workspace. Frontend: Vue 3 + Vite + Tailwind in frontend/.

BRAND DIRECTION
The cookies must look like irresistible, premium, conventional bakery treats. They must not
look like health food, protein supplements, diet products or medical nutrition products.
Our categories (low sugar, diabetic friendly, high protein, gluten free, vegan, classic) are
secondary. The visuals must sell flavour, indulgence, texture and quality first.

HOW THE SITE USES COOKIE IMAGES (match this)
- The existing images are transparent cut-outs: one cookie, no background, no cast shadow.
  See frontend/public/img/cookie.webp, cookie2.webp, cookie3.webp, cookie4.webp (~794x900 WebP
  with alpha).
- Product cards (frontend/src/components/cards/CookieCard.vue) place the image object-contain
  at 74% inside a 4:5 tinted box and add a CSS drop shadow.
- The product page (frontend/src/views/ProductDetailView.vue) places it object-contain at 78%
  inside a square dark-chocolate panel, also with a CSS drop shadow.
- So every generated image must be ONE isolated cookie on a transparent background, or, if
  the model cannot do transparency, on a flat seamless pure-white background with no cast
  shadow, ready for background removal. Never draw a fake checkerboard "transparent" pattern.
- Real product photos are attached to products through the admin upload
  (backend stores them in uploads/cookies/<uuid>). Do not touch uploads/ or the database.

REFERENCE IMAGES
Before generating anything, inspect design/cookie-references/ and tell me which file controls:
- camera angle and crop
- lighting and shadows
- cookie shape, thickness and texture
- overall mood and quality
If that folder is empty or missing a role, use the existing brand cut-outs in
frontend/public/img/ as the house-style reference for angle, crop, lighting and isolation
ONLY. Do not copy their flavours or toppings (e.g. cookie.webp's peanut-butter drizzle).
Preserve only the relevant traits from each reference and do not blend conflicting ones.
Local files are not automatically visible to Higgsfield. Use the MCP's supported upload
workflow (convert WebP to PNG in a temp folder first if needed). Never claim a reference
was used unless the upload succeeded and the generation call actually received it.

HIGGSFIELD WORKFLOW
1. List the higgsfield MCP tools. Tell me which ones generate images, upload references,
   check job status, retrieve results, remove backgrounds and report credits. For the image
   models, list their supported aspect ratios, resolutions, reference-image inputs and
   whether they can output a transparent background.
2. Check my credit balance and estimate the cost of the first test image, and of the full
   set of 12 cookies.
3. Show me the proposed prompt, the chosen model, which reference does which job and the
   expected cost. WAIT for my approval.
4. Generate ONE test image first so I can approve the art direction.
5. After I approve, generate the rest in batches of 3 and show me each batch before
   starting the next.
6. Download the real generated files:
   - Original, full resolution → design/generated/<slug>-original.png (never edited, never
     deleted).
   - Web version (transparent, trimmed to the cookie with a small even margin, about 900px
     tall, WebP quality around 85) → frontend/public/img/cookies/<slug>.webp
   - <slug> is kebab-case of the cookie name, e.g. double-chocolate-chunk.
7. Verify that each file exists, opens and has an alpha channel, and report its pixel size
   and file size.
8. Keep docs/cookie-image-manifest.md up to date as a table with these columns: cookie
   name, category, slug, original file, web file, intended placement (product card / product
   page / homepage section), status (proposed / generated / approved / rejected), model,
   references used, credits spent.

WEBSITE SAFETY
- Do not overwrite or delete anything in frontend/public/ that already exists, including
  frontend/public/img/cookie*.webp and cookieHome.webp.
- Do not edit any .vue/.js/.css files, backend code, uploads/ or the database. Claude Code
  handles integration later.
- Do not invent image paths, claim a file was saved without checking it, or use placeholder
  images in place of finished assets.
- When finished, report the files generated, any failures, the credits spent and remaining,
  and which images are waiting for my approval.

Start now with steps 1–3 only. Do not generate anything yet.
```

---

## Prompt 2: First test image (double chocolate chunk)

```text
Create ONE premium commercial bakery product photograph for the Guilt Free Cookie website.
This is the art-direction test. Generate exactly one image.

PRODUCT: Thick double chocolate chunk cookie.

REFERENCE INSTRUCTIONS
- Camera-angle reference: match the viewing angle, crop and how much of the frame the
  cookie fills.
- Lighting reference: match the direction, softness and intensity of the light.
- Texture reference: match the thickness, surface cracks and interior softness.
- The references are visual guidance only. Do not add objects or change the product.
(Name the exact file you used for each role and confirm it was uploaded.)

SUBJECT
One single thick, freshly baked double chocolate cookie: deep cocoa-brown dough, generous
glossy dark chocolate chunks pressed into and melting slightly on the surface, a crackled
top, slightly crisp edges and a visibly soft, thick, bakery-style profile. Seen from about
60° above, so the domed top and a sliver of the thick side edge are visible. The cookie is
centred and fills about 85–90% of the frame, with nothing cropped. A few natural
imperfections are fine. No plate, no props, no crumbs scattered around it.

LIGHT
Soft, warm studio key light from the upper left with gentle highlights on the melted
chocolate, a soft fill on the right and no harsh specular hotspots.

BACKGROUND (critical)
Transparent background if the model supports it. Otherwise a flat, seamless pure-white
(#FFFFFF) background with even lighting, NO cast shadow, NO contact shadow, NO surface or
table visible, and NO checkerboard pattern. The website adds its own drop shadow.

ART DIRECTION
Premium independent-bakery advertising: irresistible, realistic, warm and sophisticated.
It must look like real food shot by a professional food photographer.

DO NOT SHOW
Protein powder, oats or seeds as health cues, gym equipment, nutrition labels or graphics,
medical symbols, diet packaging, green "healthy" styling, text, logos or watermarks.

OUTPUT
One high-resolution 4:5 portrait image (at least 1600x2000 if supported).

Before generating, tell me the model, the references and their roles, the exact settings
(aspect ratio, resolution, transparency) and the credit cost. WAIT for my approval.
```

---

## After you approve the test image

Reply to Codex with:

```text
Approved. Save the test image following step 6 of the master instruction, verify it, and
add it to docs/cookie-image-manifest.md. Then propose prompts for the next batch of 3
cookies, using the same model, angle, lighting, background and framing, with only the
cookie changed. Show me the cost and wait for my approval.
```
