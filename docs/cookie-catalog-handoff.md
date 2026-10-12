# Cookie catalogue and Claude integration handoff

Prepared 12 October 2026 for Guilt Free Cookie. Twelve distinct cookie concepts: three for each requested category. The user approved the Double Chocolate Chunk art direction and authorised the remaining set. New images remain generated, not individually user-approved.

## Start here

- `docs/cookie-catalog.json`: structured product concepts, descriptions, proposed ingredients/allergens, image paths, prompts and provenance.
- `docs/cookie-image-manifest.md`: asset status, generation jobs, credit accounting and verified dimensions.
- `design/generated/`: untouched full-resolution originals and preview sheets.
- `frontend/public/img/cookies/`: transparent WebP assets, approximately 900px tall, quality 85, small even margins.
- `docs/cookie-catalog-preview.html`: local visual catalogue with the prepared copy and categories.

No application code, database rows or existing assets were changed. Do not overwrite the user's in-progress work when integrating.

## Field mapping: read before importing

| Handoff field | Existing app destination | Notes |
| --- | --- | --- |
| `name` / `adminName` | Backend `Cookie.description` | This is the displayed NAME, despite the field's name. Admin CookieForm labels it “Name”; `cookieName()` reads it. |
| `description` / `shortDescription` | No current backend field | This is new marketing body copy. Preserve separately or add a dedicated field in a later approved integration. Do not put it into the existing name field. |
| `category` | `Cookie.category` | Use exact enum values below. |
| `proposedIngredients` | Eventually `Cookie.ingredients` | Comma-separated string, only after confirming the real recipe. Not an ingredient declaration in quantity order. |
| `proposedAllergens` | Eventually `Cookie.allergies` | Draft checklist only. Confirm suppliers and cross-contact; never substitute “None” for unknown. |
| `price` | `Cookie.price` | Intentionally null. Owner must provide a price in ZAR; API requires > 0. |
| `webPath` | Multipart `image` file | Upload the actual WebP file. |
| `publicImageUrl` | Static frontend preview asset URL | Do not assign to backend `image`: current resolver prefixes the backend uploads URL. |
| `slug` | Stable handoff key | Current entity has no slug field. Do not treat this as a database ID. |
| `cookieId` | Server-generated UUID | Leave unset for new records. Record returned IDs to prevent duplicate imports. |

Category values already supported: `DIABETIC_FRIENDLY`, `HIGH_PROTEIN`, `GLUTEN_FREE`, `VEGAN`. No category enum changes are required. Leave other existing categories and products intact.

## Integrating assets

1. Confirm final recipes, allergen wording, category claims and prices with the owner. This is concept content, not verified food-labelling data. All records are `isPublishReady: false`; no invented macros, ratings, reviews, certifications or prices.
2. For each confirmed new product, use the existing admin form or `cookieApi.create(cookie, imageFile)`. It sends multipart JSON as `cookie` and the binary WebP as `image` to `/guiltFreeCookie/cookie/create`. The current API has no draft state, so do not import incomplete records as active products. Archiving alone does not replace the required price/ingredient checks.
3. Set payload `description` to the short NAME, plus the verified `category`, `ingredients`, `allergies` and real `price`. Omit invented IDs. The service stores the uploaded file under `uploads/cookies/<uuid>.<extension>` and returns its relative `image` path. Preserve that returned value.
4. Keep the longer marketing copy in a new dedicated field or explicit product metadata keyed to the returned UUID. The existing `CookieService.update()` copies fields explicitly: any added copy field needs coordinated entity, service, API/admin and storefront changes. This handoff does not implement those changes.
5. Product data comes from `useCatalog()` and the backend API. `frontend/src/data/cookies.js` contains an older mock catalogue with different categories; editing that array alone will not populate the live catalogue.
6. Reuse the existing `object-contain` treatment and CSS drop shadow. Cards use a 4:5 tinted box; the detail view uses a square dark panel. Images already have transparent margins and must not gain baked shadows. Use `altText` from the handoff where appropriate.
7. Verify each of the four category filters shows three corresponding products; click through all cards; check upload URLs, names, prices, ingredient/allergen copy, mobile layout and transparent edges. Verify no unrelated products were removed or replaced.

Relevant source files: `frontend/src/components/admin/CookieForm.vue`, `frontend/src/api/cookieApi.js`, `frontend/src/composables/useCatalog.js`, `frontend/src/constants/cookieCategories.js`, `src/main/java/za/ac/cput/guiltfreecookie/domain/Cookie.java`, `src/main/java/za/ac/cput/guiltfreecookie/service/CookieService.java`, `src/main/java/za/ac/cput/guiltfreecookie/service/ImageStorageService.java`.

## Product concepts

These descriptions are proposed sensory copy tied to the generated appearance. Check them against actual baked products before publication. AI-generated photographs are visual concepts, not evidence of the actual product's recipe, nutrition or allergen status.

### Diabetic friendly

Proposed almond-flour recipes using erythritol/stevia. Category assignment is a product-development intention, not evidence of suitability for diabetes. Confirm the actual recipe, serving size and nutrition before publishing this claim; do not add blood-sugar promises.

#### Toasted Almond & Vanilla

Golden edges give way to a tender vanilla centre, with toasted almond flakes adding a delicate crunch to every bite.

- Slug: `toasted-almond-vanilla`
- Image: `/img/cookies/toasted-almond-vanilla.webp`
- Proposed ingredients: Almond flour, butter, eggs, erythritol, stevia, vanilla extract, flaked almonds, baking powder, sea salt.
- Proposed allergen checklist: Tree nuts (almonds), milk, eggs.
- Price and nutrition: unset.

#### Cinnamon Pecan

A warmly spiced cookie with a soft middle, gently crisp edges and toasted pecan pieces baked through the dough.

- Slug: `cinnamon-pecan`
- Image: `/img/cookies/cinnamon-pecan.webp`
- Proposed ingredients: Almond flour, butter, eggs, erythritol, stevia, pecans, cinnamon, vanilla extract, baking powder, sea salt.
- Proposed allergen checklist: Tree nuts (almonds, pecans), milk, eggs.
- Price and nutrition: unset.

#### Espresso Dark Chocolate

Deep espresso notes meet glossy dark chocolate pieces in a richly baked cookie with a soft centre and crisp, cocoa-brown edges.

- Slug: `espresso-dark-chocolate`
- Image: `/img/cookies/espresso-dark-chocolate.webp`
- Proposed ingredients: Almond flour, butter, eggs, erythritol, stevia, unsweetened dark chocolate, cocoa powder, espresso powder, vanilla extract, baking powder, sea salt.
- Proposed allergen checklist: Tree nuts (almonds), milk, eggs; verify chocolate supplier allergens.
- Price and nutrition: unset.

### High protein

Proposed recipes include whey protein isolate. Confirm protein content per finished serving and the applicable claim requirements before publishing “high protein”; no protein grams are supplied.

#### Double Chocolate Chunk

Deep cocoa dough, generous dark chocolate chunks and a crackled top come together in a thick cookie with crisp edges and a soft chocolate centre.

- Slug: `double-chocolate-chunk`
- Image: `/img/cookies/double-chocolate-chunk.webp`
- Proposed ingredients: Almond flour, whey protein isolate, butter, eggs, dark chocolate, cocoa powder, erythritol, vanilla extract, baking powder, sea salt.
- Proposed allergen checklist: Tree nuts (almonds), milk, eggs; verify chocolate supplier allergens.
- Price and nutrition: unset.

#### Peanut Butter Crunch

Roasted peanut butter brings a rich, nutty flavour to this soft-baked cookie, finished with crunchy peanut pieces and a pinch of sea salt.

- Slug: `peanut-butter-crunch`
- Image: `/img/cookies/peanut-butter-crunch.webp`
- Proposed ingredients: Almond flour, whey protein isolate, peanut butter, roasted peanuts, butter, eggs, erythritol, vanilla extract, baking powder, sea salt.
- Proposed allergen checklist: Peanuts, tree nuts (almonds), milk, eggs.
- Price and nutrition: unset.

#### Vanilla White Chocolate

A vanilla-rich cookie with creamy white chocolate chunks, a tender middle and lightly golden edges.

- Slug: `vanilla-white-chocolate`
- Image: `/img/cookies/vanilla-white-chocolate.webp`
- Proposed ingredients: Almond flour, whey protein isolate, butter, eggs, white chocolate, erythritol, vanilla extract, baking powder, sea salt.
- Proposed allergen checklist: Tree nuts (almonds), milk, eggs; verify white chocolate supplier allergens.
- Price and nutrition: unset.

### Gluten free

Proposed recipes use almond, rice and/or buckwheat flour. Confirm every supplier ingredient and production cross-contact controls before claiming gluten-free; ingredient choice alone does not establish the claim.

#### Lemon Almond

Bright lemon zest lifts a tender almond cookie, while toasted almond flakes bring a delicate crunch to its golden top.

- Slug: `lemon-almond`
- Image: `/img/cookies/lemon-almond.webp`
- Proposed ingredients: Almond flour, rice flour, butter, eggs, cane sugar, lemon zest, lemon juice, flaked almonds, gluten-free baking powder, sea salt.
- Proposed allergen checklist: Tree nuts (almonds), milk, eggs; cross-contact controls require confirmation.
- Price and nutrition: unset.

#### Salted Chocolate Buckwheat

Toasty buckwheat dough and generous dark chocolate chunks make a deeply satisfying pairing, finished with a light touch of flaky sea salt.

- Slug: `salted-chocolate-buckwheat`
- Image: `/img/cookies/salted-chocolate-buckwheat.webp`
- Proposed ingredients: Buckwheat flour, rice flour, butter, eggs, brown sugar, dark chocolate, vanilla extract, gluten-free baking powder, sea salt.
- Proposed allergen checklist: Milk, eggs; verify chocolate supplier allergens and cross-contact controls.
- Price and nutrition: unset.

#### Coconut Macadamia

Toasted coconut and creamy macadamia pieces fill a golden cookie with a soft centre and crisp, fragrant edges.

- Slug: `coconut-macadamia`
- Image: `/img/cookies/coconut-macadamia.webp`
- Proposed ingredients: Almond flour, rice flour, butter, eggs, cane sugar, desiccated coconut, macadamia nuts, vanilla extract, gluten-free baking powder, sea salt.
- Proposed allergen checklist: Tree nuts (almonds, macadamias), milk, eggs; cross-contact controls require confirmation.
- Price and nutrition: unset.

### Vegan

Proposed recipes use plant-based baking fat, flax and dairy-free chocolate where applicable. Verify all compound ingredients and processing aids. Vegan does not mean allergen-free.

#### Dark Chocolate Orange

Rich cocoa dough and dark chocolate chunks are lifted by bright orange zest for a fragrant cookie with a fudgy centre.

- Slug: `dark-chocolate-orange`
- Image: `/img/cookies/dark-chocolate-orange.webp`
- Proposed ingredients: Wheat flour, plant-based baking fat, brown sugar, dairy-free dark chocolate, cocoa powder, orange zest, ground flaxseed, water, vanilla extract, baking powder, sea salt.
- Proposed allergen checklist: Wheat; verify plant-based fat and chocolate supplier allergens and cross-contact controls.
- Price and nutrition: unset.

#### Raspberry Coconut

Tart raspberry pieces and toasted coconut brighten a soft vanilla cookie, with lightly crisp edges and a generous bakery-style bite.

- Slug: `raspberry-coconut`
- Image: `/img/cookies/raspberry-coconut.webp`
- Proposed ingredients: Wheat flour, plant-based baking fat, cane sugar, raspberries, desiccated coconut, ground flaxseed, water, vanilla extract, baking powder, sea salt.
- Proposed allergen checklist: Wheat; verify plant-based fat supplier allergens and cross-contact controls.
- Price and nutrition: unset.

#### Peanut Chocolate Ripple

Roasted peanut butter ribbons run through golden dough, with dark chocolate chunks adding rich pockets of flavour to a soft, chewy centre.

- Slug: `peanut-chocolate-ripple`
- Image: `/img/cookies/peanut-chocolate-ripple.webp`
- Proposed ingredients: Wheat flour, peanut butter, plant-based baking fat, brown sugar, dairy-free dark chocolate, ground flaxseed, water, vanilla extract, baking powder, sea salt.
- Proposed allergen checklist: Peanuts, wheat; verify plant-based fat and chocolate supplier allergens and cross-contact controls.
- Price and nutrition: unset.

## Prompt for Claude

```text
Read docs/cookie-catalog-handoff.md, docs/cookie-catalog.json and docs/cookie-image-manifest.md. Inspect the existing catalogue/API before integrating. Use the 12 matching cookie assets and preserve their exact category/slug mappings. Backend description currently stores the NAME; keep the longer description separate. Prices, ingredients, allergens and dietary claims need owner confirmation before publication. Do not invent them. Use the existing multipart image upload workflow and retain returned UUIDs/image paths. Do not overwrite unrelated changes or existing products. Show the required integration changes and preserve the generated originals.
```

