package application.company;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController() {
        this.companyService = new CompanyService();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CompanyDto> getAllCompanies() {
        return companyService.getAllCompanies();
    }

}
