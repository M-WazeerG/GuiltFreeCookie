/**
 * schema.org structured data (JSON-LD). Every page carries the business and
 * the website; each page adds itself, its breadcrumb trail and, where it
 * shows them, its product or FAQs. Only facts the site already states are
 * used: nothing here is something a visitor couldn't read on the page.
 */
import { BUSINESS } from "../constants/business";
import { formatCategoryLabel } from "../constants/cookieCategories";
import { faqs } from "../data/faqs";
import { cookieImage, cookieName } from "../composables/useCatalog";
import { SITE_LANGUAGE, SITE_NAME, SITE_URL, HOME_DESCRIPTION, SHARE_IMAGE, absoluteUrl } from "./site";

const BUSINESS_ID = `${SITE_URL}/#business`;
const WEBSITE_ID = `${SITE_URL}/#website`;

const PAGE_TYPES = {
  Products: "CollectionPage",
  ProductDetail: "ItemPage",
  About: "AboutPage",
  Contact: "ContactPage",
};

function businessNode() {
  const sameAs = Object.values(BUSINESS.socials).filter(Boolean);
  return {
    "@type": "Bakery",
    "@id": BUSINESS_ID,
    name: BUSINESS.tradingName,
    url: `${SITE_URL}/`,
    description: HOME_DESCRIPTION,
    logo: absoluteUrl("/icon-512.png"),
    image: absoluteUrl(SHARE_IMAGE.url),
    telephone: BUSINESS.phone,
    email: BUSINESS.email,
    address: { "@type": "PostalAddress", ...BUSINESS.postalAddress },
    hasMap: BUSINESS.mapsUrl,
    areaServed: { "@type": "City", name: "Cape Town" },
    currenciesAccepted: "ZAR",
    ...(sameAs.length ? { sameAs } : {}),
  };
}

function websiteNode() {
  return {
    "@type": "WebSite",
    "@id": WEBSITE_ID,
    url: `${SITE_URL}/`,
    name: SITE_NAME,
    inLanguage: SITE_LANGUAGE,
    publisher: { "@id": BUSINESS_ID },
  };
}

function breadcrumbNode(url, crumbs) {
  return {
    "@type": "BreadcrumbList",
    "@id": `${url}#breadcrumb`,
    itemListElement: crumbs.map((crumb, i) => ({
      "@type": "ListItem",
      position: i + 1,
      name: crumb.label,
      ...(i < crumbs.length - 1 ? { item: absoluteUrl(crumb.path) } : {}),
    })),
  };
}

function productNode(url, cookie) {
  return {
    "@type": "Product",
    "@id": `${url}#product`,
    name: cookieName(cookie),
    description: cookie.longDescription || cookie.description,
    image: [absoluteUrl(cookieImage(cookie))],
    category: `${formatCategoryLabel(cookie.category)} cookies`,
    brand: { "@type": "Brand", name: SITE_NAME },
    offers: {
      "@type": "Offer",
      url,
      price: Number(cookie.price).toFixed(2),
      priceCurrency: "ZAR",
      seller: { "@id": BUSINESS_ID },
    },
  };
}

function faqNode(url) {
  return {
    "@type": "FAQPage",
    "@id": `${url}#faq`,
    mainEntity: faqs.map((faq) => ({
      "@type": "Question",
      name: faq.question,
      acceptedAnswer: { "@type": "Answer", text: faq.answer },
    })),
  };
}

export function structuredData({ route, url, title, description, crumbs, cookie }) {
  const page = {
    "@type": PAGE_TYPES[route.name] || "WebPage",
    "@id": `${url}#webpage`,
    url,
    name: title,
    description,
    inLanguage: SITE_LANGUAGE,
    isPartOf: { "@id": WEBSITE_ID },
    about: { "@id": BUSINESS_ID },
  };

  const graph = [businessNode(), websiteNode(), page];

  if (crumbs.length) {
    graph.push(breadcrumbNode(url, crumbs));
    page.breadcrumb = { "@id": `${url}#breadcrumb` };
  }
  if (cookie) {
    graph.push(productNode(url, cookie));
    page.mainEntity = { "@id": `${url}#product` };
  }
  if (route.meta?.faq) {
    graph.push(faqNode(url));
  }

  return { "@context": "https://schema.org", "@graph": graph };
}
