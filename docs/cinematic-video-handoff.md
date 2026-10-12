# Cookie drop video — Claude handoff

Generated through Higgsfield MCP on 12 October 2026 using Wan 3.0 and matched reference frames. The user approved the splash direction and video production. Review the [video player](cinematic-video-review.html) before integrating. This work changed assets and documentation only; the existing application has other pre-existing changes.

## Files

All files below live in `design/generated/cinematic/video/`. No public deployment path has been assigned or existing public asset overwritten.

| File | Purpose | Size |
| --- | --- | --- |
| `cookie-drop-original.mp4` | Untouched generated master | 8,128,077 bytes |
| `cookie-drop-web.mp4` | H.264, fast-start, CRF 20, keyframe every 30 frames | 2,070,478 bytes |
| `cookie-drop-scroll.mp4` | H.264, fast-start, CRF 21, every frame a keyframe for seeking | 6,278,610 bytes |
| `first-frame.webp` | Loading/poster frame from the actual video | 34,216 bytes |
| `splash-frame.webp` | Actual video frame at 3.3 seconds | 24,240 bytes |
| `motion-contact-timed.jpg` | Twelve sampled frames labelled at 0.5-second intervals | 83,582 bytes |
| `start-keyframe.png`, `impact-keyframe.png` | Prepared 1920×1080 image-to-video references | See files |
| `start-original.png` | Unmodified generated still-milk reference | 4,931,048 bytes |
| `video-manifest.json` | Model, prompts, media IDs, cost and timing notes | JSON |
| `delivery-verification.json` | File sizes and SHA-256 checksums | JSON |

All three videos are 1920×1080, exactly 6.000 seconds, 30 fps, 180 frames, no audio. They are opaque H.264/yuv420p; they do not contain alpha. The full-stage background is baked into the video. The processing sandbox prepared the web versions; original generation metadata remains in the untouched master.

## Actual motion and integration point

Sampled visual inspection found one recognisable two-tone cookie, a downward fall, milk impact and an expanding crown splash. The glass stays approximately fixed, the 80/20 boundary remains visible, and the base stays within frame. This is a generated shot, with minor shape changes and a late upward rebound as the cookie approaches the end reference. Do not describe it as a perfect physical simulation or a pixel-identical match to the original hero artwork.

| Source time | Observed action / integration guidance |
| --- | --- |
| 0–1.0 s | Suspended cookie above still milk; use the first-frame poster for loading. |
| 1.0–2.4 s | Cookie descends; clear downward travel is visible by 2 s. |
| Approximately 2.4–2.7 s | Cookie crosses into milk; impact begins. |
| 3.0–3.5 s | Crown opens. Start the eight-cookie reveal around 3.3 s. |
| 4.0–6.0 s | Cookie rises slightly toward the final reference. Cover or recede the video as the eight separate cookie layers take over. |

These are practical windows from half-second samples, not collision measurements. The 3.3-second poster is extracted at an exact timestamp. Use the review player's slider to tune the transition on the final page.

## Preserve the hero and reveal eight flavours

Keep the original `/img/cookieHome.webp`, headline and CTAs in the resting hero with the requested desktop 80% tan / 20% chocolate split. The clip starts after scrolling reveals the glass. It is not a replacement for the resting hero. Its cookie scale differs from the original artwork, and its moving cookie has no decorative chocolate ribbons: align position and scale and design that handoff deliberately.

The video is a full-width desktop stage, not a floating transparent overlay. Keep its aspect ratio and split aligned to the stage; do not apply `object-fit: cover` blindly because cropping moves the 80% boundary and can cut off the glass. Compression and generated reflections can differ from the exact CSS colours. Check the boundary and stage edges in-browser. A mobile crop has not been prepared; use a separately framed or simplified mobile sequence rather than clipping the right-side action.

Use one GSAP ScrollTrigger master timeline. Begin by testing `cookie-drop-scroll.mp4` for direct `currentTime` scrubbing after metadata loads. The all-intra encode makes seeking easier but is larger; test forward/backward seeking on actual mobile devices before choosing it over the smaller web version. A canvas frame sequence remains an optional later optimisation, not a delivered asset.

At approximately 3.3 seconds, stagger the eight exact transparent catalogue WebPs from behind the splash into measured product slots. Their identities, paths and category assignments are in `cinematic-cookie-scroll-plan.json`. They are not baked into this clip. Retain the agreed eight flavours, two from each proposed category, and reveal labels as the movement settles. Do not publish unverified nutrition, prices or dietary claims.

Provide a static reduced-motion option, loading poster, media-failure fallback and functional shop links. Clean up ScrollTriggers/listeners on Vue unmount. Application integration and browser performance testing remain Claude's work.

## Credits and verification

This step cost 23 credits: 2 for the still-milk reference and 21 for the six-second video. The earlier storyboard cost 3 credits. Reported balance after generation: 48 credits. The user explicitly approved transfer of the completed video ZIP from Higgsfield's sandbox to its media library and then into this repository.

Decoded metadata, dimensions, duration, frame count and absence of audio were checked. Original and delivery hashes were verified after download. Twelve sampled frames were visually inspected. The web and scroll encodes retain the source timing; browser integration and mobile scroll performance have not yet been tested.
