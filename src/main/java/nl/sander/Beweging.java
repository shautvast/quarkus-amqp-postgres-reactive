package nl.sander;

import java.time.LocalDate;

public class Beweging {

    public Beweging() {
    }

    public Beweging(Long id, LocalDate vlDatum, String bewegingcode) {
        this.id = id;
        this.vlDatum = vlDatum;
        this.bewegingcode = bewegingcode;
    }

    private Long id;

    private LocalDate vlDatum;

    private String bewegingcode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getVlDatum() {
        return vlDatum;
    }

    public void setVlDatum(LocalDate vlDatum) {
        this.vlDatum = vlDatum;
    }

    public String getBewegingcode() {
        return bewegingcode;
    }

    public void setBewegingcode(String bewegingcode) {
        this.bewegingcode = bewegingcode;
    }
}
