package uz.pdp.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.entity.Months;
import uz.pdp.hrmanagement.entity.Salary;
import uz.pdp.hrmanagement.entity.User;
import uz.pdp.hrmanagement.enums.MonthName;
import uz.pdp.hrmanagement.payload.ApiResponse;
import uz.pdp.hrmanagement.payload.SalaryDto;
import uz.pdp.hrmanagement.repository.MonthRepository;
import uz.pdp.hrmanagement.repository.SalaryRepository;
import uz.pdp.hrmanagement.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SalaryService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SalaryRepository salaryRepository;
    @Autowired
    MonthRepository monthRepository;

    public ApiResponse paySalary(SalaryDto salaryDto){
        Optional<User> optionalUser = userRepository.findById(salaryDto.getEmployee());
        if (!optionalUser.isPresent())
            return new ApiResponse("Xodim topilmadi",false);
        User employee = optionalUser.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRoles().equals("DIRECTOR") || user.getRoles().equals("HR_MANAGER")){
            Salary salary=new Salary();
            salary.setEmployee(employee);
            salary.setAmountSalary(salaryDto.getAmountSalary());
            Optional<Months> optionalMonths = monthRepository.findById(salaryDto.getMonthId());
            if (!optionalMonths.isPresent())
                return new ApiResponse("Bunday oy nomi mavjud emas",false);
            salary.setMonth(optionalMonths.get());
            salaryRepository.save(salary);
            return new ApiResponse("Oylik saqlandi "+employee.getLastName()+" "+ employee.getFirstName()+" uchun",true);
        }
        return new ApiResponse("Sizda oylik saqlashga huquq yoq",true);
    }

    public ApiResponse getSalarybyEmployee(UUID id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())
            return new ApiResponse("Xodim topilmadi",false);
        User user = optionalUser.get();
        List<Salary> salaryList = salaryRepository.findAllByEmployee(user);
        return new ApiResponse("Oylik royhati bitta xodim boyicha: "+salaryList,true);
    }
}
