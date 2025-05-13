package com.dac.fly.saga.feign;

import com.dac.fly.shared.dto.response.ApiResponse;
import com.dac.fly.shared.dto.response.AuthDTO;
import com.dac.fly.shared.dto.response.EmployeeDto;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "http://auth-service:8080")
public interface AuthClient {
    @GetMapping("/usuarios/{email}")
    ApiResponse<AuthDTO> findByEmail(@PathVariable("email") String email);

    default AuthDTO findUserByEmail(String email) {
        try {
            return findByEmail(email).getData();
        } catch (FeignException e) {
            if (e.status() == 404) return null;
            throw e;
        }
    }

}


