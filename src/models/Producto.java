package models;
import java.io.Serializable;

public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String nombre;
    private double precio;
    private int cantidad;
    private int stockMin;
    private int stockMax;
    private String tipo;
    private boolean activo;

    public Producto() {
    }

    public Producto(String id, String nombre, double precio, int cantidad, int stockMin, int stockMax, String tipo, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.stockMin = stockMin;
        this.stockMax = stockMax;
        this.tipo = tipo;
        this.activo = activo;
    }

    public String getId(){
        return id;}

    public void setId(String id){
        this.id = id;}
    
    public String getNombre(){
        return nombre;}

    public void setNombre(String nombre){
        this.nombre = nombre;}
    
    public double getPrecio(){
        return precio;}

    public void setPrecio(double precio){
        this.precio = precio;}
    
    public int getCantidad(){
        return cantidad;}

    public void setCantidad(int cantidad){
        this.cantidad = cantidad;}

    public int getStockMin(){
        return stockMin;}

    public void setStockMin(int stockMin){
        this.stockMin = stockMin;}
    
    public int getStockMax(){
        return stockMax;}

    public void setStockMax(int stockMax){
        this.stockMax = stockMax;}
    
    public String getTipo(){
        return tipo;}

    public void setTipo(String tipo){
        this.tipo = tipo;}
    
    public boolean isActivo(){
        return activo;}

    public void setActivo(boolean activo){
        this.activo = activo;}

    @Override
    public String toString() {
        String estado;
        if (activo) { 
            estado = "Activo";
        } else {
            estado = "Dado de Baja";
        }
        return "Producto: " + nombre + " (" + tipo + ") | Precio: $" + precio + 
           " | Stock: " + cantidad + " (Min: " + stockMin + " Max: " + stockMax + ") | Estado: " + estado;
    }
}
