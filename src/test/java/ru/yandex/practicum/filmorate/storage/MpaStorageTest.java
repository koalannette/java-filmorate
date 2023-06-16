package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MpaStorageTest {
    @Autowired
    private final MpaStorage mpaStorage;

    @Test
    public void getMpaById() {
        Mpa mpa = mpaStorage.getMpaById(1);
        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void getAllMpas() {
        Collection<Mpa> mpas = mpaStorage.getMpas();
        assertEquals(mpas.size(), 5);
    }


}

