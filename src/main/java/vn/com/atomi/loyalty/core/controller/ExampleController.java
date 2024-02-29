package vn.com.atomi.loyalty.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.core.dto.input.LoginInput;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class ExampleController {

  @PostMapping("/public/example-success")
  public ResponseEntity<ResponseData<String>> exampleSuccess(
      @RequestParam String query, @RequestBody LoginInput loginInput) {
    return ResponseUtils.success("LoyaltyCoreApplication.ExampleController.exampleSuccess");
  }

  @GetMapping("/public/example-error")
  public ResponseEntity<ResponseData<String>> exampleError() throws InterruptedException {
    Thread.sleep(10000);
    return ResponseUtils.error(
        1,
        "LoyaltyCoreApplication.ExampleController.exampleError",
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @GetMapping("/public/example-error-fallback")
  public ResponseEntity<ResponseData<String>> exampleErrorFallBack() throws InterruptedException {
    Thread.sleep(10000);
    return ResponseUtils.success("LoyaltyCoreApplication.ExampleController.exampleErrorFallBack");
  }
}
