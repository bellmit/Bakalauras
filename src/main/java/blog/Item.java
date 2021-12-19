package blog;

public class Item {
    private String title;
    private String description;
    private String date;

    public Item(String title, String description, String date) {

        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {

        // null check
        if (o == null) {
            return false;
        }

        // this instance check
        if (this == o) {
            return true;
        }

        Item n = (Item) o;
        // instanceof Check and actual value check
        return o instanceof Item &&
                n.getTitle().equals(getTitle()) &&
                n.getDescription().equals(getDescription()) &&
                n.getDate().equals(getDate());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getDate().hashCode();
        result = 31 * result + getDescription().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getTitle() + ',' + getDescription() + ',' + getDate();
    }
}