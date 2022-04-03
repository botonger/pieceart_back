package com.example.pieceart.notice;

import com.example.pieceart.entity.File;
import com.example.pieceart.entity.Notice;
import com.example.pieceart.entity.Member;

import java.util.List;

public interface NoticeService {

    List<NoticeDTO> findByPage(int page);
    List<NoticeDTO> findAll();
    NoticeDTO findById(Long id);
    NoticeDTO create(NoticeDTO noticeDTO, String fileName, String email);
    boolean delete(Long id, String password);
    NoticeDTO update(NoticeDTO noticeDTO, String filename, String email);

    default NoticeDTO entityToDto (Notice notice, File file){
        NoticeDTO noticeDTO = NoticeDTO.builder()
                .id(notice.getId())
                .content(notice.getContent())
                .title(notice.getTitle())
                .password(notice.getPassword())
                .viewCount(notice.getViewCount())
                .created(notice.getCreated())
                .modified(notice.getModified())
                .writer(notice.getMember().getName())
                .fileName(file!=null?file.getFileName():null)
                .build();

        return noticeDTO;
    }

    default Notice dtoToEntity (NoticeDTO noticeDTO, Member member){
        Notice notice = Notice.builder()
                .password(noticeDTO.getPassword())
                .content(noticeDTO.getContent())
                .title(noticeDTO.getTitle())
                .member(member)
                .build();

        return notice;
    }
}
