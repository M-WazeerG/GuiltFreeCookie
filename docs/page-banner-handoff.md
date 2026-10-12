# Page banner images — Claude handoff

Three coordinated images generated through Higgsfield MCP on 12 October 2026. Open [the banner preview](page-banner-preview.html) to see proposed placement with separate HTML headings. The application components were not edited. Existing public images were not overwritten.

| Page | Public image URL | Dimensions | File size | Direction |
| --- | --- | --- | --- | --- |
| Products | `/img/banners/products-banner.webp` | 1400×861 | 251,696 bytes | Floating chocolate, almond, white chocolate and raspberry assortment |
| About | `/img/banners/about-banner.webp` | 1400×788 | 199,226 bytes | Broken chocolate cookies, almond cookie and wooden almond spoon |
| Contact | `/img/banners/contact-banner.webp` | 1400×889 | 265,942 bytes | Small, welcoming arrangement of cookies to share |

All three WebPs have real alpha transparency, including clear outer padding and soft edge pixels. Their combined size is 716,864 bytes (about 700 KiB). They can sit directly over the existing tan background without a white rectangle. A restrained CSS drop shadow is optional. Do not remove the background again or stretch the artwork.

## Integration points

- `ProductListView.vue` currently places `/img/cookie3.webp` and `/img/cookie.webp` as overlapping decorative images in its header. Replace that pair with the new Products composition when integrating. Its existing negative bottom offset and `overflow-hidden` can crop this wider group; use an appropriately sized right-hand image box and inspect the complete silhouette.
- `AboutView.vue` passes `/img/cookie4.webp` to `PageHero`. Use the About banner URL in that prop.
- `ContactView.vue` passes `/img/cookie2.webp` to `PageHero`. Use the Contact banner URL in that prop.
- `PageHero.vue` applies `rotate-6` to the entire image. The new compositions already contain angled cookies; start without an additional wrapper rotation, then judge in context. Keep `object-fit: contain`, intrinsic width/height and sufficient breathing room.
- Current inner-page artwork is hidden below the `md` breakpoint. Retain that behaviour unless a mobile artwork layout is explicitly implemented. The review page shows a stacked mobile example only; it is not a claim about current application behaviour.

Keep page headings, supporting copy and buttons as real HTML. These assets do not impose the homepage's 80/20 split or change its hero/video. They are transparent decorative groups for the existing inner-page banners. If decorative alongside a clear heading, retain empty alt text and the surrounding `aria-hidden`; otherwise use a concise description such as the ones in the preview.

The About ingredient cues are conceptual, not confirmation of actual recipes or dietary suitability. The assortment references existing generated catalogue imagery but is a fresh composition, not an exact set of independently identifiable SKU photos. Product names, categories, prices and nutrition should continue to come from verified catalogue data.

## Originals and verification

Original PNGs are in `design/generated/banners/`, each 2752×1536 with an opaque white backdrop. The sibling WebPs are delivery copies of the public files. `banner-manifest.json` records prompts, reference job IDs, result URLs and processing details. `banner-verification.json` records dimensions, alpha extrema and SHA-256 checksums. `banners-alpha-review.jpg` shows the cutouts on tan and dark chocolate.

The original compositions and cutouts were visually inspected at preview scale. Alpha ranges from 0 to 255 for each WebP. All downloaded originals, cutouts and public copies were hash-checked. The review-page asset links were checked; actual application integration and responsive browser testing remain to be done by Claude.

Generation cost: 6 credits total; reported remaining balance: 42 credits. The user explicitly approved the finished banner ZIP transfer through the Higgsfield media library into this project.
