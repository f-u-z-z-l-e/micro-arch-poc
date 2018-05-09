package ch.fuzzle.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {

    @JsonProperty("id")
    private String id;
    @JsonProperty("message")
    private String mesage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Info info = (Info) o;
        return Objects.equals(id, info.id) &&
                Objects.equals(mesage, info.mesage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mesage);
    }

    @Override
    public String toString() {
        return "Info{" +
                "id='" + id + '\'' +
                ", mesage='" + mesage + '\'' +
                '}';
    }

    public static class Builder {
        private Info info;

        public static Info info(String id, String message) {
            Info info = new Info();
            info.setId(id);
            info.setMesage(message);
            return info;
        }
    }
}
