package com.satc.integrador.ai.preferencia;

import com.satc.integrador.ai.preferencia.dto.PreferenciaGetDto;
import com.satc.integrador.ai.preferencia.dto.PreferenciaPostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/preferencia")
public class PreferenciaController {

    @Autowired
    private PreferenciaService preferenciaService;

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getOneFromUser(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(preferenciaService.getOneFromUser(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getCurrent() {
        return ResponseEntity.ok(preferenciaService.getCurrent());
    }

    @GetMapping("list")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllFromUser(@RequestParam Integer page,
                                                 @RequestParam Integer count) {
        return ResponseEntity.ok(preferenciaService.getAllFromUser(page, count));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> post(@RequestBody PreferenciaPostDto dto) {
        return ResponseEntity.ok(preferenciaService.post(dto));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> patch(@PathVariable("id") Integer id,
                                        @RequestBody PreferenciaPostDto dto) {
        return ResponseEntity.ok(preferenciaService.patch(id, dto));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) {
        preferenciaService.delete(id);
        return ResponseEntity.ok().build();
    }

    //ADMIN ACCESS ONLY
    @GetMapping("adm/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PreferenciaGetDto> getOneAdm(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(preferenciaService.getOneAdm(id));
    }

    @GetMapping("adm")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PreferenciaGetDto>> getAllAdm(@RequestParam Integer page,
                                                             @RequestParam Integer count) {
        return ResponseEntity.ok(preferenciaService.getAllAdm(page, count));
    }

    @PostMapping("adm")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PreferenciaGetDto> postAdm(@RequestBody PreferenciaPostDto dto) {
        return ResponseEntity.ok(preferenciaService.postAdm(dto));
    }

    @PatchMapping("adm/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PreferenciaGetDto> patchAdm(@PathVariable("id") Integer id,
                                                      @RequestBody PreferenciaPostDto dto) {
        return ResponseEntity.ok(preferenciaService.patchAdm(id, dto));
    }

    @DeleteMapping("adm/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PreferenciaGetDto> deleteAdm(@PathVariable("id") Integer id) {
        preferenciaService.deleteAdm(id);
        return ResponseEntity.ok().build();
    }
}
