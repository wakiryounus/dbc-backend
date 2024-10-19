package k.wakir.dbcbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/businessCard")
public class BusinessCardController {

    @GetMapping("/")
    public String businessCard() {
        return "Worked";
    }

}
