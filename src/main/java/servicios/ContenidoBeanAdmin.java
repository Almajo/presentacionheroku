package servicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.primefaces.context.RequestContext;


@ManagedBean(name="contenidoView")
@ViewScoped
public class ContenidoBeanAdmin {
	private Contenido nuevoContenido;
	
	//private String URL_Back = "http://172.16.145.186:8080/ServidorTsi2-0.0.1-SNAPSHOT/contenido/contenidos/";
	private String URL_Back = "http://localhost:8080/ServidorTsi2-0.0.1-SNAPSHOT";
	
	private Map<Integer,List<Categoria>> data = new HashMap<Integer, List<Categoria>>();
	private Categoria categoria;
	private String[] selectedCategorias;
	private List<Categoria> categorias;
	private Categoria selectedCate;
	
	private String elencoi;
	private List<String> elencos;
	private String directori;
	private List<String> directores;
	
	//CONTENIDOS
	private Contenido selectedCont;
	List<Contenido> contenidos;
	List<Contenido> contenidoFiltrado;
	private Contenido contenido;
	
	//TIPOS CONTENIDO
	List<TipoContenido> tiposcontenido;
	private TipoContenido tipo;
	private String nombreTipoContenido;
	private String atributosTipoContenido;
	private String categoriasTipoContenido;
	private TipoContenido selectedTipo;
	
	@PostConstruct
	public void init(){
		tipo = new TipoContenido();
		selectedTipo = new TipoContenido();
		contenido = new Contenido();
		contenidos = new ArrayList<Contenido>();
		
		selectedCate = new Categoria();
		categoria = new Categoria();
		categorias  = new ArrayList<Categoria>();
    	/*categorias.add(new Categoria(1,"USA"));
    	categorias.add(new Categoria(2,"Germany"));
    	categorias.add(new Categoria(3,"Brazil"));
    	*/
    	elencoi = null;
        elencos = new ArrayList<String>();
    	directori = null;
        directores = new ArrayList<String>();
	}
	
	public boolean guardarContenido(){
    	Client client = ClientBuilder.newClient();
    	System.out.println("El titulo es: "+contenido.getTitulo());
    	System.out.println("La descripcion es: "+contenido.getDescripcion());
    	System.out.println("El tipo es: "+contenido.getTipoContenido());
    	contenido.setElenco(elencos);
    	contenido.setDirectores(directores);
    	Response postResponse = client
    	.target(URL_Back + "/contenido/agregarContenido")
    	.request().post(Entity.json(contenido));
    	
    	if ((postResponse.getStatus() != 201) && (postResponse.getStatus() != 200)){
    		System.out.println("Error al consumir mediante post.");
    	}
    	else{
    		System.out.println("Se consumio correctamente mediante post.");
    		contenido = new Contenido();
    		elencos = new ArrayList<String>();
    		directores = new ArrayList<String>();
    		reset("header-contenido");
    	}
    	
		return true;
	}
	
	public List<String> toList(String[] array){
		List<String> lista = new ArrayList<String>();
		for(String c: array){
			lista.add(c);
		}
		return lista;
	}
	
	public String toStr(List<String> lista){
		String dev = null;
		for(String l: lista){
			dev =dev+","+l;
		}
		return dev;
	}
	
	public boolean guardarTipoContenido(){
		System.out.println("NOMBRE de tipo: "+tipo.getNombre());
		System.out.println("ATRIBUTOS de tipo: "+tipo.getAtributos());
		tipo.setAtributos(null);
    	Client client = ClientBuilder.newClient();
    	Response postResponse = client
    	.target(URL_Back +"/contenido/tiposcontenido/"+tipo.getNombre())
    	.request().post(Entity.json(tipo));
    	
    	if ((postResponse.getStatus() != 201) && (postResponse.getStatus() != 200)){
    		System.out.println("Error al consumir mediante post.");
    	}
    	else{
    		System.out.println("Se consumio correctamente mediante post.");
    		tipo = new TipoContenido();
    		reset("nuevodialogo");
    	}
    	
		return true;
	}
	
	public boolean guardarCategoria(){
    	Client client = ClientBuilder.newClient();
    	Response postResponse = client
    	.target(URL_Back +"/contenido/categorias/")
    	.request().post(Entity.json(categoria));
    	
    	if ((postResponse.getStatus() != 201) && (postResponse.getStatus() != 200)){
    		System.out.println("Error al consumir mediante post.");
    	}
    	else{
    		categoria = new Categoria();
    		reset("nuevodialogo");
    		System.out.println("Se consumio correctamente mediante post.");
    	}
    	
		return true;
	}
	
	public List<Contenido> obtenerContenidos(){
		Client client = ClientBuilder.newClient();
    	List<Contenido> contenidos = client
    	.target(URL_Back+"/contenido/obtenerContenidos")
    	.request(MediaType.APPLICATION_JSON).get(new GenericType<List<Contenido>>() {});
			this.contenidos = contenidos;
		return contenidos;
	}
	
	public List<TipoContenido> obtenerTiposContenidos(){
		Client client = ClientBuilder.newClient();
    	List<TipoContenido> tiposcontenidos = client
    	.target(URL_Back+"/contenido/tipoContenido")
    	.request(MediaType.APPLICATION_JSON).get(new GenericType<List<TipoContenido>>() {});
			this.tiposcontenido = tiposcontenidos;
		return tiposcontenidos;
	}
	
	public List<Categoria> obtenerCategorias(){
		Client client = ClientBuilder.newClient();
    	List<Categoria> categorias = client
    	.target(URL_Back+"/contenido/getCategorias")
    	.request(MediaType.APPLICATION_JSON).get(new GenericType<List<Categoria>>() {});
		this.categorias = categorias;
		return categorias;
	}
	
	public List<Categoria> obtenerCategoriasPorTipo(String nombTipo){
		Client client = ClientBuilder.newClient();
		System.out.println(URL_Back +"/contenido/getCategoriasTipoContenido");
		System.out.println("Paso: "+Entity.text(nombTipo));
		Response postResponse = client
		    	.target(URL_Back +"/contenido/getCategoriasTipoContenido")
		    	.request().post(Entity.text(nombTipo));
		System.out.println("Entity respuesta: "+postResponse.getEntity());
		List<Categoria> categorias = postResponse.readEntity(new GenericType<List<Categoria>>() {});
		if (!categorias.isEmpty()){
			this.categorias = categorias;
		}
		return categorias;
	}

	public boolean eliminarCategoria(int i){
		System.out.println("ID categoria: "+i);
		System.out.println("NOMBRE categoria: "+categoria.getNombre());
    	Client client = ClientBuilder.newClient();
    	Response postResponse = client
    	.target(URL_Back +"/contenido/categorias/"+i)
    	.request().delete();
    	
    	if ((postResponse.getStatus() != 201) && (postResponse.getStatus() != 200)){
    		System.out.println("Error al consumir mediante delete.");
    	}
    	else{
    		System.out.println("Se consumio correctamente mediante delete.");
    	}
    	
		return true;
	}
	
	public void onTipoChange() {
        if((contenido.getTipoContenido() !=null) && (!contenido.getTipoContenido().equals(""))){
            categorias = obtenerCategoriasPorTipo(contenido.getTipoContenido());
        }
        else{
            categorias = new ArrayList<Categoria>();
        }
    }
	
	public void reset(String lugar) {
        RequestContext.getCurrentInstance().reset(lugar);
    }
	
	public Contenido getNuevoContenido() {
		return nuevoContenido;
	}

	public void setNuevoContenido(Contenido nuevoContenido) {
		this.nuevoContenido = nuevoContenido;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public String getElencoi() {
		return elencoi;
	}

	public void setElencoi(String elencoi) {
		this.elencoi = elencoi;
	}

	public List<String> getElencos() {
		return elencos;
	}

	public void setElencos(List<String> elencos) {
		this.elencos = elencos;
	}

	public String getDirectori() {
		return directori;
	}

	public void setDirectori(String directori) {
		this.directori = directori;
	}

	public List<String> getDirectores() {
		return directores;
	}

	public void setDirectores(List<String> directores) {
		this.directores = directores;
	}

	public List<Contenido> getContenidos() {
		return contenidos;
	}

	public void setContenidos(List<Contenido> contenidos) {
		this.contenidos = contenidos;
	}

	public Contenido getContenido() {
		return contenido;
	}

	public void setContenido(Contenido contenido) {
		this.contenido = contenido;
	}

	public List<Contenido> getContenidoFiltrado() {
		return contenidoFiltrado;
	}

	public void setContenidoFiltrado(List<Contenido> contenidoFiltrado) {
		this.contenidoFiltrado = contenidoFiltrado;
	}

	public Contenido getSelectedCont() {
		return selectedCont;
	}

	public void setSelectedCont(Contenido selectedCont) {
		this.selectedCont = selectedCont;
	}

	public List<TipoContenido> getTiposcontenido() {
		return tiposcontenido;
	}

	public void setTiposcontenido(List<TipoContenido> tiposcontenido) {
		this.tiposcontenido = tiposcontenido;
	}

	public TipoContenido getTipo() {
		return tipo;
	}

	public void setTipo(TipoContenido tipo) {
		this.tipo = tipo;
	}

	public Categoria getSelectedCate() {
		return selectedCate;
	}

	public void setSelectedCate(Categoria selectedCate) {
		this.selectedCate = selectedCate;
	}

	public TipoContenido getSelectedTipo() {
		return selectedTipo;
	}

	public void setSelectedTipo(TipoContenido selectedTipo) {
		this.selectedTipo = selectedTipo;
	}
	
	public void seleccionarTipoContenido(TipoContenido tipocont){
		this.nombreTipoContenido = tipocont.getNombre();
	}
	
	public void updateTipoContenido(){
		this.tipo.setNombre(this.nombreTipoContenido);
		//this.tipo.setAtributos(Arrays.asList(this.atributosTipoContenido));
		guardarTipoContenido();
	}

	public String getNombreTipoContenido() {
		return nombreTipoContenido;
	}

	public void setNombreTipoContenido(String nombreTipoContenido) {
		this.nombreTipoContenido = nombreTipoContenido;
	}

	public String getAtributosTipoContenido() {
		return atributosTipoContenido;
	}

	public void setAtributosTipoContenido(String atributosTipoContenido) {
		this.atributosTipoContenido = atributosTipoContenido;
	}

	public String[] getSelectedCategorias() {
		return selectedCategorias;
	}

	public void setSelectedCategorias(String[] selectedCategorias) {
		this.selectedCategorias = selectedCategorias;
	}

	public String getCategoriasTipoContenido() {
		return categoriasTipoContenido;
	}

	public void setCategoriasTipoContenido(String categoriasTipoContenido) {
		this.categoriasTipoContenido = categoriasTipoContenido;
	}
	
	
}
