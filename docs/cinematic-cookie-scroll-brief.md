# Cinematic cookie drop and eight-flavour reveal

Production brief, 12 October 2026. The storyboard was approved and the six-second video has been generated. See [video review](cinematic-video-review.html) and [Claude video handoff](cinematic-video-handoff.md) for delivered files and measured timing. This asset workflow has not changed website code. Machine-readable asset paths and scroll beats: `cinematic-cookie-scroll-plan.json`.

## Storyboard review

Open [the storyboard review page](cinematic-storyboard.html) for the hero, milk impact and eight selected cutouts. The opening plate uses the original artwork composed in Higgsfield's sandbox on an exact 80/20 split. The impact plate was generated through Higgsfield Nano Banana 2.1 with both the opening plate and original artwork as references. Generation cost: 3 credits; balance afterward: 71 credits.

Originals, previews, prompts, media IDs and visual review notes are in `design/generated/cinematic/`; see `storyboard-manifest.json` and `storyboard-verification.json`. Opening dimensions: 3840×2160. Impact dimensions: 5504×3072 (the actual output differs slightly from requested 16:9). The generated impact retains the two-tone cookie and split but introduces background texture/colour drift and places the glass slightly left of the boundary. Resolve those differences, cookie scale and edge droplet margins before production.

These are creative storyboard beats, not a matched pair ready for direct video interpolation. After approval, prepare a video start with the same glass, camera and scale as the impact, milk still and cookie suspended above it. Reveal that stage only as scrolling begins. Preserve the original hero at rest and design the transition out of its decorative ribbons. Both storyboard plates are opaque. The separate completed video also has a baked background; no transparent foreground was delivered.

## Creative direction

The existing half-chocolate, half-vanilla hero artwork (`/img/cookieHome.webp`) remains in the opening hero. Preserve the headline, CTA, colours and the cookie straddling the colour boundary. The requested desktop layout is 80% tan on the left and 20% chocolate on the right, with the main cookie silhouette centred on the split line. As the visitor scrolls, that same cookie descends into a glass of milk. A sculptural milk crown rises and opens the composition. Eight recognisable cookies fan outward, then settle into an ordered flavour selection. Product names and range labels appear after the movement settles.

Use one continuous pinned visual stage for this transition. Keep the hero typography and product copy as real HTML. The source clip remains six seconds; the visitor's scroll controls the pace of the entire sequence. Falling, splashing and arranging are parts of one scroll narrative, not three independently playing sections.

## Hero constraints — latest user direction

The opening viewport must remain recognisably the existing hero. Keep the colour split visible; do not replace it with a full-width tan background, a video box, a glass of milk or an eight-cookie montage. Glass and new choreography begin after scrolling starts. The hero artwork is brand imagery, not an identified product in the eight-flavour catalogue.

Code inspection found `lg:w-[36%]` for the current chocolate panel in `HeroBanner.vue`; the user explicitly requested 80/20. Claude should reconcile that to the requested 20% desktop panel while preserving the rest of the composition. No production source was edited during this brief update. Keep the current top/bottom mobile arrangement unless separately directed; adapt the cookie anchor to that responsive boundary.

The hero artwork already includes surrounding splash ribbons and crumbs. Preserve the complete artwork at rest. A moving cookie-only foreground needs a separately prepared matching cutout, or a matched video handoff that accounts for those decorative elements. Do not translate the complete still image as if its baked-in ribbons were a physically evolving splash, and do not replace its cookie with Double Chocolate Chunk.

## Eight selected flavours

| Range | Flavour one | Flavour two |
| --- | --- | --- |
| Diabetic friendly | Toasted Almond & Vanilla | Cinnamon Pecan |
| High protein | Double Chocolate Chunk | Vanilla White Chocolate |
| Gluten free | Lemon Almond | Coconut Macadamia |
| Vegan | Raspberry Coconut | Peanut Chocolate Ripple |

Use the exact existing assets in the companion JSON. These range assignments are proposed product concepts from the catalogue handoff; dietary claims remain unverified. The choice provides light/dark dough, almond slices, pecans, white chocolate, coconut, raspberry and peanut ribbons for visual variety.

## Production approach

Generate the complex physical action—one falling cookie and a milk splash—as one video. Use all eight existing transparent cookie assets as separately controllable visual layers for the reveal and final arrangement. This gives Claude exact final positions and preserves recognisable flavours. Asking a single generated video to render eight precise products and land each in a responsive UI slot risks identity drift and makes placement difficult to adjust.

The eight cookies must participate in the main animation, not merely appear as thumbnails below it. At peak splash, stagger their entrance from behind the central action, vary their initial rotations and scale, and send them along distinct outward paths. Double Chocolate Chunk remains one of the eight final products, but it is not a replacement for the existing hero cookie. The hero cookie submerges; the eight catalogue cutouts appear in the following reveal, so no unexplained flavour transformation is needed.

## Scroll choreography

| Progress | Visual action | Copy |
| --- | --- | --- |
| 0–15% | Hold the existing hero artwork at the 80/20 split line. Match its cookie identity and silhouette at the motion handoff; reveal the glass below only as scrolling begins. | Existing hero headline and CTA. |
| 15–40% | Cookie falls, rotates subtly, and enters the stationary milk glass. | Hero copy fades as the cookie descends. |
| 40–55% | Milk crown rises into the foreground. Hold the peak briefly. | Optional short line: “Make a splash.” |
| 55–80% | Eight cutouts fan outward in staggered arcs; glass and video recede. | Optional short line: “Find your favourite.” |
| 80–100% | Cookies settle into measured positions; names and categories reveal. | Product names, range labels and a shop CTA. |

Copy is proposed and should not be baked into the video. Tune timing after viewing the actual generated impact frame; percentage values are initial design guidance, not measured events.

## Original video preparation brief (completed)

Create matched start and end frames with identical camera, crop, glass, lighting and background. Start: the same half-chocolate, half-vanilla cookie visible in the existing hero, above still milk. Its initial placement must register to the hero split-line anchor. End: same glass and cookie at impact with an elevated milk crown. The approved multi-cookie splash graphic is mood reference only; it is unsuitable as the literal start frame because it already contains a splash.

Proposed clip settings: Wan 3.0, 6 seconds, 1080p, audio off, one output. Final framing must fit the preserved hero and responsive colour boundary. The earlier cost-only quote was 21 credits for a portrait 3:4 clip; it is not a quote for the complete compositing workflow. Requote after choosing the final framing and confirming reference inputs. Frame creation and any postprocessing are separate.

Keep the CSS hero split: tan `#D0AB7B` at 80% and chocolate `#2E0E0B` at 20% on desktop. Preferred delivery is a foreground sequence with verified alpha so the cookie and splash can cross that boundary. Alpha extraction around glass and milk is a separate compositing task and has not been tested; ordinary MP4 is not assumed transparent. If alpha proves unsuitable, prepare a full-stage composite with the exact measured split, separately reframed for each breakpoint. A portrait clip centred at the viewport boundary does not necessarily have its own boundary at 80%; derive local media coordinates from the actual stage. Do not cover the split with a solid-tan rectangle or silently flatten the hero. Review any visual compromise before implementation.

### Higgsfield motion prompt

```text
Create one continuous six-second premium bakery advertising shot from
the supplied matched start and end frames. Fixed camera, fixed glass,
consistent scale and warm soft upper-left studio lighting.

The exact half-chocolate, half-vanilla cookie from the supplied
existing hero reference begins suspended above a clear glass of
still milk. Preserve its swirl pattern, silhouette and baked texture. It descends with a restrained natural rotation and
enters the centre of the milk. The impact creates an elegant crown
splash that rises and opens outward toward the viewer, with a small
number of suspended droplets. Finish near the expanded splash pose
so the website can transition into a separate eight-cookie reveal.

Preserve the cookie identity, glass shape and liquid volume. Keep
the entire glass and splash inside the frame with generous margins.
Match the supplied production-frame backdrop and its fixed colour
boundary exactly. No camera movement, cuts, extra cookies,
text, logos, watermarks or background objects. No audio.
```

Prompt timing is guidance; inspect the actual motion and reject duplication, morphing, incorrect collisions or clipped splashes before integration.

## Claude implementation handoff

Use GSAP ScrollTrigger with one pinned parent stage and one master timeline. Begin with direct video-time scrubbing; evaluate backward seeking and mobile performance. If seeking is visibly uneven, export an optimised frame sequence and draw it to canvas with staged loading. A six-second sequence sampled at 24fps is approximately 144 frames; avoid preloading full-resolution frames indiscriminately.

Align the moving layer to the existing hero cookie silhouette, anchored to the 80/20 boundary; do not replace the resting hero artwork with a new poster. A matched clip poster can support the transition after scroll starts. Match position, scale and rotation before the handoff to avoid a silhouette jump. Keep the CSS split behind the animation and match any full-stage video boundary to it. At peak splash, animate the eight exact WebP layers from individually offset origins with a slight stagger, depth-scaled sizes and restrained rotation. Give each layer a stable slug key. Avoid starting all eight at the same coordinate and scale.

Measure final positions from real layout slots on refresh rather than hard-coded viewport pixels. Desktop target: four columns by two rows. Mobile target: two columns by four rows, with a shorter or simplified reveal and no offscreen pinned controls. Allow the final mobile catalogue to continue in ordinary page flow. Keep labels readable and avoid overlapping product interactions during transit.

If product DOM nodes move from the pinned stage into the final catalogue, preserve their screen-space bounds during reparenting. Keep the same media throughout that handoff. A GSAP Flip transition is an optional implementation choice after checking its fit; it is not required for the video itself.

Provide a reduced-motion state showing the final eight-cookie arrangement, a loading poster, and a media-failure fallback. Keep hero navigation and shop links accessible. Dispose of ScrollTriggers and media listeners on Vue unmount. Do not publish invented prices, nutrition or verified dietary claims.

Deliverables for video production: untouched generated MP4; optimised web clip; first-frame poster; splash-frame poster; measured impact/splash timestamps; optional frame sequence; confirmed dimensions/duration/file size; updated credit log. Use new paths and retain the existing product originals. The completed video paths and timing are assigned in `cinematic-cookie-scroll-plan.json` and `cinematic-video-handoff.md`.

GSAP reference: https://gsap.com/docs/v3/Plugins/ScrollTrigger/
