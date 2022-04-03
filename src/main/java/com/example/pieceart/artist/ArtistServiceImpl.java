package com.example.pieceart.artist;

import com.example.pieceart.entity.Artist;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ArtistServiceImpl implements ArtistService{
    private final ArtistRepository artistRepository;


    // 작가 찾기
    @Override
    @Transactional
    public ArtistDTO findArtistById(Long id){
        Optional<Artist> artist = artistRepository.findById(id);
        if(artist.isPresent()){
            Artist artist1 = artist.get();
            return entityToDTO(artistRepository.save(artist1));
        }
        return null;
    }

    // 작가 생성
    @Override
    @Transactional
    public ArtistDTO createArtist(ArtistDTO artistDTO){
        Artist created  = artistRepository.save(dtoToEntity(artistDTO));
        return entityToDTO(created);
    }


    // 작가 삭제
    public void deleteArtist(Long id){
        artistRepository.deleteById(id);
    }

    // 작가 수정
    @Override
    @Transactional
    public ArtistDTO updateArtist(Long id, ArtistDTO artistDTO){
        return artistRepository.findById(id)
                .map(artist->{
                    Artist updated = Artist.builder()
                            .id(artist.getId())
                            .name(artistDTO.getName())
                            .achieve(artistDTO.getAchieve())
                            .degree(artistDTO.getDegree())
                            .description(artistDTO.getDescription())
                            .build();
                    return entityToDTO(artistRepository.save(updated));
                }).get();
    }
}