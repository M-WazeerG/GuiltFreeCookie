package za.ac.cput.guiltfreecookie.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "cookies")
public class Cookie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cookie_id")
    private String cookieId;

    @Enumerated(EnumType.STRING)
    private CookieCategory category;

    private String description;

    @Column(name = "long_description", length = 1000)
    private String longDescription;

    private String ingredients;
    private String allergies;
    private Double price;
    private String image;
    private boolean archived;

    protected Cookie() {
    }

    private Cookie(Builder builder) {
        this.cookieId = builder.cookieId;
        this.category = builder.category;
        this.description = builder.description;
        this.longDescription = builder.longDescription;
        this.ingredients = builder.ingredients;
        this.allergies = builder.allergies;
        this.price = builder.price;
        this.image = builder.image;
        this.archived = builder.archived;
    }

    public String getCookieId() {
        return cookieId;
    }

    public CookieCategory getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getAllergies() {
        return allergies;
    }

    public Double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public boolean isArchived() {
        return archived;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "cookieId='" + cookieId + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", allergies='" + allergies + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", archived=" + archived +
                '}';
    }

    public static class Builder {
        private String cookieId;
        private CookieCategory category;
        private String description;
        private String longDescription;
        private String ingredients;
        private String allergies;
        private Double price;
        private String image;
        private boolean archived;

        public Builder setCookieId(String cookieId) {
            this.cookieId = cookieId;
            return this;
        }

        public Builder setCategory(CookieCategory category) {
            this.category = category;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setLongDescription(String longDescription) {
            this.longDescription = longDescription;
            return this;
        }

        public Builder setIngredients(String ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public Builder setAllergies(String allergies) {
            this.allergies = allergies;
            return this;
        }

        public Builder setPrice(Double price) {
            this.price = price;
            return this;
        }

        public Builder setImage(String image) {
            this.image = image;
            return this;
        }

        public Builder setArchived(boolean archived) {
            this.archived = archived;
            return this;
        }

        public Builder copy(Cookie cookie) {
            this.cookieId = cookie.cookieId;
            this.category = cookie.category;
            this.description = cookie.description;
            this.longDescription = cookie.longDescription;
            this.ingredients = cookie.ingredients;
            this.allergies = cookie.allergies;
            this.price = cookie.price;
            this.image = cookie.image;
            this.archived = cookie.archived;
            return this;
        }

        public Cookie build() {
            return new Cookie(this);
        }
    }
}