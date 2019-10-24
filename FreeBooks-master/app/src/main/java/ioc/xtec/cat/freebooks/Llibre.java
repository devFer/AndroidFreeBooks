package ioc.xtec.cat.freebooks;

/**
 * Created by jordi on 17/03/2018.
 */

public class Llibre {
    private String titol;
    private String autor;
    private String descripcio;
    private String imatgePortada;
    private String edAndYear;
    private String numPags;
    private String idioma;
    private String ISBN;

    /**
     * Constructor
     *
     * @param titol amb el títol del llibre
     * @param autor amb l'autor del llibre
     * @param descripcio amb la descripció del llibre
     * @param imatgePortada amb la imatge de la portada
     * @param edAndYear amb l'editor i l'any
     * @param numPags amb el número de pàgines
     * @param idioma amb l'idioma
     * @param ISBN amb el número d'ISBN
     */
    public Llibre(String titol, String autor, String descripcio, String imatgePortada, String edAndYear, String numPags, String idioma, String ISBN) {
        this.titol = titol;
        this.autor = autor;
        this.descripcio = descripcio;
        this.imatgePortada = imatgePortada;
        this.edAndYear = edAndYear;
        this.numPags = numPags;
        this.idioma = idioma;
        this.ISBN = ISBN;
    }

    /**
     * Getters i Setters
     */

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getImatgePortada() {
        return imatgePortada;
    }

    public void setImatgePortada(String imatgePortada) {
        this.imatgePortada = imatgePortada;
    }

    public String getEdAndYear() {
        return edAndYear;
    }

    public void setEdAndYear(String edAndYear) {
        this.edAndYear = edAndYear;
    }

    public String getNumPags() {
        return numPags;
    }

    public void setNumPags(String numPags) {
        this.numPags = numPags;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
}
