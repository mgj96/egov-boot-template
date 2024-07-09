package egovframework.example.sample.web;

import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.SampleVO;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.swing.text.Document;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;


@ActiveProfiles("local")
@DisplayName("실시간 주차장 정보")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class EgovSampleControllerTest {

    /**
     * EgovSampleService
     */
    @Resource(name = "sampleService")
    private EgovSampleService sampleService;

    /**
     * EgovPropertyService
     */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    private SampleVO sampleVO;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        sampleVO = new SampleVO();
        sampleVO.setPageUnit(propertiesService.getInt("pageUnit"));
        sampleVO.setPageSize(propertiesService.getInt("pageSize"));

        // pagination setting
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(sampleVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(sampleVO.getPageUnit());
        paginationInfo.setPageSize(sampleVO.getPageSize());

        sampleVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        sampleVO.setLastIndex(paginationInfo.getLastRecordIndex());
        sampleVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

    }

    @Test
    @DisplayName("샘플 리스트")
    void list() throws Exception {
        // 리턴이 있는 경우 성공

        mockMvc.perform(
                    RestDocumentationRequestBuilders.post("/sample/list")
                        .param("searchKeyword", "테스트")
                        .param("searchCondition", "0")
                    )
                .andDo(
                    MockMvcRestDocumentation.document(
                       /* Preprocessors.preprocessRequest(Preprocessors.prettyPrint())
                        , Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
                        ,*/ requestParameters(
                            parameterWithName("searchKeyword").description("검색어")
                            , parameterWithName("searchCondition").description("0 or 1").optional()
                        ).toString()
                    )
                )
        ;
        // List
        Assertions.assertNotNull(sampleService.selectSampleList(sampleVO));
    }

    @Test
    void detail() {
    }

    @Test
    void form() {
    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}