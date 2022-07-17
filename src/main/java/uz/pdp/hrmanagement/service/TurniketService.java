package uz.pdp.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.entity.Turniket;
import uz.pdp.hrmanagement.entity.User;
import uz.pdp.hrmanagement.payload.ApiResponse;
import uz.pdp.hrmanagement.repository.TurniketRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TurniketService {
    @Autowired
    TurniketRepository turniketRepository;

    public ApiResponse enterToWork() {
        Turniket turniket = new Turniket();
        turniket.setStatus(true);
        turniket.setEnterDateTime(LocalDateTime.now());
        turniketRepository.save(turniket);
        return new ApiResponse("KIRISH", true);
    }

    public ApiResponse exitFromWork() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            User user = (User) authentication.getPrincipal();
            Optional<Turniket> optionalTurniket = turniketRepository.findByCreatedByAndStatus(user.getId(), true);
            if (!optionalTurniket.isPresent())
                return new ApiResponse("Bunday turniket topilmadi", false);
            optionalTurniket.get().setStatus(false);
            optionalTurniket.get().setExitDateTime(LocalDateTime.now());
            turniketRepository.save(optionalTurniket.get());
            return new ApiResponse("CHIQISH!", true);
        }
        return new ApiResponse("Auzentifikatsiya bo'sh!", false);
    }
}
