package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> getMpas() {
        return mpaStorage.getMpas();
    }

    public Mpa getMpaById(int genreId) {
        return mpaStorage.getMpaById(genreId);
    }

}
