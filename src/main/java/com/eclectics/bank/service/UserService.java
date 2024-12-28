package com.eclectics.bank.service;

import com.eclectics.bank.domain.Address;
import com.eclectics.bank.domain.ConfirmationToken;
import com.eclectics.bank.domain.Customer;
import com.eclectics.bank.dto.UserDto;
import com.eclectics.bank.repository.AddressRepository;
import com.eclectics.bank.repository.ConfirmationTokenRepository;
import com.eclectics.bank.repository.CustomerRepository;
import com.eclectics.bank.security.JWTTokenUtil;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.slf4j.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService implements UserDetailsService {


    private final  CustomerRepository customerRepo;

    private final AddressRepository addressRepo;

    private final ConfirmationTokenRepository confirmationTokenRepo;
    private final PasswordEncoder passwordEncoder;


    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(CustomerRepository customerRepo, AddressRepository addressRepo, ConfirmationTokenRepository confirmationTokenRepo, JWTTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
        this.confirmationTokenRepo = confirmationTokenRepo;


        this.passwordEncoder = passwordEncoder;
    }



    public void register(UserDto user){

        if(userExists(user.getEmail(), user.getPhone())){
            throw new RuntimeException("User already exists");
        }

        Customer customer = new Customer();

        customer.setFullName(user.getFullName());
        customer.setDateOfBirth(user.getDateOfBirth());
        customer.setGender(user.getGender());
        customer.setNationalId(user.getNationalId());
        customer.setPhone(user.getPhone());

        customer.setEmail(user.getEmail());
        customer.setOccupation(user.getOccupation());
        customer.setEmployerName(user.getEmployerName());
        customer.setMonthlyIncome(user.getMonthlyIncome());

        customer = customerRepo.save(customer);
        for(Address address : user.getAddresses()){
            address.setCustomer(customer);
        addressRepo.save(address);
        }
        customer.setAddresses(user.getAddresses());

        int randomOTP = (int) ((Math.random() * (999999 - 1)) + 1);
        String token = String.format("%06d", randomOTP);

//                customer,
//                customer.getEmail(),
//                "",
//                token,
//                LocalDateTime.now(),
//                LocalDateTime.now().plusMinutes(24 * 60 * 1), // 1 DAYS Expiry,
//                null,
//                false
        ConfirmationToken confirmationToken = new ConfirmationToken(
        );
        confirmationToken.setCustomer(customer);
        confirmationToken.setUsername(customer.getEmail());
        confirmationToken.setPassword("");
        confirmationToken.setToken(token);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(24 * 60 * 1));
        confirmationToken.setConfirmedAt(null);
        confirmationToken.setVerified(false);
        confirmationToken.setCreatedBy("System");
        confirmationTokenRepo.save(confirmationToken);
        log.info("Confirmation token generated: {}",token);
//        send message to registered user



    }

    private boolean userExists(String email, String phone){
        boolean exists = customerRepo.findByEmailOrPhone(email, phone).isPresent();

        return exists;
    }


    public void confirmUser(String token){
        ConfirmationToken confirmationToken = confirmationTokenRepo.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid token"));
//        confirmationToken.setVerified(true);
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now());
        confirmationTokenRepo.save(confirmationToken);
    }

    public void setPassword(String password, String token){
        ConfirmationToken confirmationToken = confirmationTokenRepo.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid token"));
        String encodedPassword = passwordEncoder.encode(password);
        confirmationToken.setPassword(encodedPassword);
        confirmationToken.setVerified(true);
        confirmationToken.setEnabled(true);
        confirmationTokenRepo.save(confirmationToken);

    }



    public ConfirmationToken findByUsername(String username) {
        return confirmationTokenRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("Invalid username"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


            return confirmationTokenRepo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("USER NOT FOUND!!!", username)));

    }
}
