import defaultTheme from 'tailwindcss/defaultTheme'

// The root font size is 62.5% (see main.css), so 1rem = 10px instead of 16px.
// Tailwind's built-in scales are written for a 16px rem, so every rem value
// is multiplied by 1.6 here to keep the same pixel sizes: p-4 is still 16px
// (now 1.6rem), max-w-7xl is still 1280px (now 128rem).
const REM_SCALE = 1.6
const scaleRem = (value) =>
  typeof value === 'string'
    ? value.replace(/(-?\d*\.?\d+)rem/g, (_, n) => `${+(parseFloat(n) * REM_SCALE).toFixed(4)}rem`)
    : value
const scaleAll = (obj) => Object.fromEntries(Object.entries(obj).map(([k, v]) => [k, scaleRem(v)]))

/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    // Three sizes only, on a modular scale from an 18px base:
    //   sm      = base / 1.25 (major third)
    //   base    = 1.8rem (18px)
    //   display = base x 1.333^3 -> base x 1.333^4 (perfect fourth), fluid
    fontSize: {
      sm: ['var(--fs-sm)', { lineHeight: '1.5', letterSpacing: '0.01em' }],
      base: ['var(--fs-base)', { lineHeight: '1.6' }],
      display: ['var(--fs-display)', { lineHeight: '1.1', letterSpacing: '-0.01em' }],
    },
    // 10px-rem versions of Tailwind's rem-based scales (see REM_SCALE above).
    // Everything built on spacing (padding, margin, gap, width, height, inset,
    // translate, min/max sizes) inherits the scaled values automatically.
    spacing: scaleAll(defaultTheme.spacing),
    borderRadius: scaleAll(defaultTheme.borderRadius),
    lineHeight: scaleAll(defaultTheme.lineHeight),
    columns: scaleAll(defaultTheme.columns),
    maxWidth: (ctx) => {
      const spacing = ctx.theme('spacing')
      return Object.fromEntries(
        Object.entries(defaultTheme.maxWidth(ctx)).map(([k, v]) => [k, k in spacing ? v : scaleRem(v)]),
      )
    },
    extend: {
      colors: {
        // GuiltFreeCookie brand palette
        primary: {
          DEFAULT: '#C59F71', // warm caramel
          50: '#FAF3EA',
          100: '#F2E4D0',
          200: '#E5C9A4',
          300: '#D9B085',
          400: '#C59F71',
          500: '#B0855A',
          600: '#9A6E45',
          700: '#7E5836',
          800: '#5F4128',
          900: '#3F2B1B',
        },
        // The hero / showcase tan used for big colour-block sections
        tan: {
          DEFAULT: '#D0AB7B',
          light: '#DCBD94',
          dark: '#BE9563',
        },
        chocolate: {
          DEFAULT: '#2E0E0B', // dark chocolate
          50: '#7A4A3F',
          100: '#5E352D',
          200: '#4D2A22',
          300: '#3D1F17',
          400: '#351710',
          500: '#2E0E0B',
          600: '#260A08',
          700: '#1E0805',
          800: '#160503',
          900: '#0E0302',
        },
        cream: {
          DEFAULT: '#E2D9C8',
          50: '#FBF9F4',
          100: '#F5F0E6',
          200: '#EDE5D5',
          300: '#E2D9C8',
          400: '#D2C5AC',
          500: '#BBA98A',
        },
        background: '#E3D9C8', // page background, same as the showcase card
        // Cards, panels, fields: the showcase card colour instead of white
        surface: '#E3D9C8',
      },
      fontFamily: {
        // Body, UI, subheads: everything that isn't a hero-style headline
        sans: ['"Nunito Variable"', 'ui-sans-serif', 'system-ui', 'sans-serif'],
        // Attention-grabbing headlines only (hero, home sections, page titles)
        display: ['"Luckiest Guy"', '"Nunito Variable"', 'ui-sans-serif', 'sans-serif'],
      },
      borderRadius: {
        '4xl': '3.2rem', // 32px
        '5xl': '4rem', // 40px
      },
      boxShadow: {
        soft: '0 4px 20px -2px rgba(46, 14, 11, 0.08)',
        'soft-lg': '0 12px 40px -4px rgba(46, 14, 11, 0.12)',
        'soft-xl': '0 24px 60px -8px rgba(46, 14, 11, 0.18)',
        cookie: '0 20px 50px -10px rgba(46, 14, 11, 0.35)',
      },
      dropShadow: {
        cookie: '0 28px 36px rgba(46, 14, 11, 0.32)',
        'cookie-sm': '0 14px 18px rgba(46, 14, 11, 0.25)',
      },
      backgroundImage: {
        'cookie-gradient': 'linear-gradient(135deg, #C59F71 0%, #B0855A 100%)',
        'chocolate-gradient': 'linear-gradient(160deg, #3D1F17 0%, #2E0E0B 60%, #1E0805 100%)',
      },
      keyframes: {
        'fade-in': {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        'fade-up': {
          '0%': { opacity: '0', transform: 'translateY(24px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
        'cookie-in': {
          '0%': { opacity: '0', transform: 'translateY(40px) rotate(-14deg) scale(0.92)' },
          '100%': { opacity: '1', transform: 'translateY(0) rotate(0) scale(1)' },
        },
        marquee: {
          '0%': { transform: 'translateX(0)' },
          '100%': { transform: 'translateX(-50%)' },
        },
        shimmer: {
          '0%': { backgroundPosition: '-600px 0' },
          '100%': { backgroundPosition: '600px 0' },
        },
        ripple: {
          '0%': { transform: 'scale(0)', opacity: '0.6' },
          '100%': { transform: 'scale(4)', opacity: '0' },
        },
      },
      animation: {
        'fade-in': 'fade-in 0.6s ease-out both',
        'fade-up': 'fade-up 0.8s cubic-bezier(0.16, 1, 0.3, 1) both',
        'cookie-in': 'cookie-in 1.1s cubic-bezier(0.16, 1, 0.3, 1) both',
        marquee: 'marquee 40s linear infinite',
        shimmer: 'shimmer 1.6s linear infinite',
        ripple: 'ripple 0.6s ease-out',
      },
      transitionTimingFunction: {
        cookie: 'cubic-bezier(0.16, 1, 0.3, 1)',
      },
    },
  },
  plugins: [],
}
