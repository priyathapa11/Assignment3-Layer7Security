import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountController(AccountRepository accountRepository,
                             PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {

        if (accountRepository.emailExists(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        accountRepository.saveUser(
                request.getFullName(),
                request.getEmail(),
                hashedPassword
        );

        return ResponseEntity.ok("Account created securely");
    }
}
