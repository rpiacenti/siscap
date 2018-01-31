package br.com.correios.siscap.managedbeans;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

@ManagedBean
@Named("PautaEmpurradaMB")
@ConversationScoped
public class PautaEmpurradaMB extends MB{

	 
	 public String handleFileUpload(FileUploadEvent event) {
	        System.out.println("UPLOAD do Arquivo com sucesso "+event.getFile().getFileName());
			return event.getFile().getFileName();
	    }
	
	public String actionInfo(){
		FacesMessage message = new FacesMessage("Succesful is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
		return null;
    }
}

	
	/*
	
    Logger log;
    
    private Part file;
    
    public void upload(){
        log.info("call upload...");      
        log.log(Level.INFO, "content-type:{0}", file.getContentType());
        log.log(Level.INFO, "filename:{0}", file.getName());
        log.log(Level.INFO, "submitted filename:{0}", file.getName());
        log.log(Level.INFO, "size:{0}", file.getSize());
        byte[] results=new byte[(int)file.getSize()];
        try {
            
            
            InputStream in=file.getInputStream();
            in.read(results);         
        } catch (IOException ex) {
           log.log(Level.SEVERE, " ex @{0}", ex);
        }
        System.out.println(results);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Uploaded!"));
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
    
}


 * import javax.faces.bean.ManagedBean; import javax.faces.bean.ViewScoped;
 * 
 * import org.primefaces.event.FileUploadEvent; import
 * org.primefaces.model.UploadedFile;
 * 
 * @ManagedBean
 * 
 * @ViewScoped public final class PautaEmpurradaMB extends MB{
 * 
 * private static final long serialVersionUID = 1L; private UploadedFile
 * uploadedFile;
 * 
 * public PautaEmpurradaMB(){}
 * 
 * public UploadedFile getUploadedFile() { return uploadedFile; }
 * 
 * public void setUploadedFile(UploadedFile uploadedFile) { this.uploadedFile =
 * uploadedFile; }
 * 
 * public void fileUploadListener(FileUploadEvent event){
 * uploadedFile=event.getFile(); }
 * 
 * public void insert(){ if(uploadedFile!=null){
 * System.out.println(uploadedFile.getFileName()); } else{
 * System.out.println("The file object is null."); } } }
 */