package prefeitura.pvh.fileupload.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "tb_image_data")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Lob
    @Column(name = "imagedata", length = 1000)
    private byte[] data;

}


