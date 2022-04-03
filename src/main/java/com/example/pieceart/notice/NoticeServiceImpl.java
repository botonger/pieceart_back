package com.example.pieceart.notice;

import com.example.pieceart.entity.File;
import com.example.pieceart.entity.Member;
import com.example.pieceart.entity.Notice;
import com.example.pieceart.repository.FileRepository;
import com.example.pieceart.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository repository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;

    //페이지별 공지사항 불러오기
    @Override
    @Transactional(readOnly = true)
    public List<NoticeDTO> findByPage(int page){
        Iterable<Notice> notices = repository.findAll(PageRequest.of(page-1, 10, Sort.by("id").descending()));
        List<NoticeDTO> list = new ArrayList<>();
        notices.forEach(notice -> {
            Optional<File> file = fileRepository.getFileOfNotice(notice.getId());
            if(file.isPresent()){
                list.add(entityToDto(notice, file.get()));
            } else{
                list.add(entityToDto(notice, null));
            }
        });
        return list;
    }

    //공지사항 전체 불러오기
    @Override
    @Transactional(readOnly = true)
    public List<NoticeDTO> findAll(){
        Iterable<Notice> notices = repository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        List<NoticeDTO> list = new ArrayList<>();
        notices.forEach(notice -> {
            Optional<File> file = fileRepository.getFileOfNotice(notice.getId());

            if(file.isPresent()) {
                list.add(entityToDto(notice, file.get()));
            }
            else list.add(entityToDto(notice, null));
        });
        return list;
    }

    //특정 공지사항 불러오기
    @Override
    @Transactional
    public NoticeDTO findById(Long id){
        Optional<Notice> notice = repository.findById(id);
        if(notice.isPresent()){
            Notice increaseNotice = notice.get();
            increaseNotice.increaseViewCount(increaseNotice.getViewCount()+1);

            Optional<File> file = fileRepository.getFileOfNotice(increaseNotice.getId());

            if(file.isPresent()) return entityToDto(repository.save(increaseNotice), file.get());
            else return entityToDto(repository.save(increaseNotice), null);
        }
        return null;
    }

    //공지사항 생성
    @Override
    @Transactional
    public NoticeDTO create(NoticeDTO noticeDTO, String fileName, String email){
        Member member = memberRepository.findByEmail(email, false).get();
        Notice created  = repository.save(dtoToEntity(noticeDTO, member));
        if(fileName==null) {
            return entityToDto(created, null);
        }
        File file = fileRepository.save(File.builder().fileName(fileName).notice(created).build());
        return entityToDto(created, file);

    }
    //공지사항 삭제
    @Override
    @Transactional
    public boolean delete(Long id, String password){
        Optional<Notice> notice = repository.findById(id);
        if(notice.get().getPassword().equals(password)) {
            Optional<File> file = fileRepository.getFileOfNotice(id);
            if (file.isPresent()) {
                fileRepository.deleteById(file.get().getId());
            }
            repository.delete(notice.get());
            return true;
        } else return false;
    }

    //공지사항 수정
    @Override
    @Transactional
    public NoticeDTO update(NoticeDTO noticeDTO, String fileName, String email){
        Notice noticeInDB = repository.findById(noticeDTO.getId()).get();

        if(noticeDTO.getPassword().equals(noticeInDB.getPassword())){
            return repository.findById(noticeDTO.getId())
                    .map(notice->{
                            notice.setTitle(noticeDTO.getTitle());
                            notice.setContent(noticeDTO.getContent());
                            notice.setPassword(noticeDTO.getPassword());

                            if (fileName!=null) {//기존 파일 삭제 로직 추가하기
                                fileRepository.deleteFile(notice.getId());
                                File file = fileRepository.save(File.builder().fileName(fileName).notice(notice).build());
                                return entityToDto(repository.save(notice), file);
                            }
                            else {
                                if(fileRepository.getFileOfNotice(notice.getId()).isPresent()){
                                    fileRepository.deleteFile(noticeDTO.getId());
                                }
                                return entityToDto(repository.save(notice), null);
                            }
                    }).get();
        } else return null;
    }
}
