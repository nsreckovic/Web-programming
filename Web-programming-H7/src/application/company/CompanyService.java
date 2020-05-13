package application.company;

import java.util.ArrayList;
import java.util.List;

public class CompanyService {

    public List<CompanyDto> getAllCompanies() {
        List<Company> companies = CompanyRepository.getCompanies();
        List<CompanyDto> companyDtos = new ArrayList<>();
        for (Company c : companies) {
            CompanyDto cDto = new CompanyDto(c.getName(), c.getLink());
            companyDtos.add(cDto);
        }
        return companyDtos;
    }

}
