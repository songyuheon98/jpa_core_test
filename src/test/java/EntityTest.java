import com.sparta.entity.Memo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class EntityTest {

    EntityManagerFactory emf;
    EntityManager em;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("memo");
        em = emf.createEntityManager();
    }

    @Test
    @DisplayName("EntityTransaction 성공 테스트")
    void test1() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 EntityTransaction 을 가져옵니다.

        et.begin(); // 트랜잭션을 시작합니다.

        try { // DB 작업을 수행합니다.

            Memo memo = new Memo(); // 저장할 Entity 객체를 생성합니다.
            memo.setId(1L); // 식별자 값을 넣어줍니다.
            memo.setUsername("Robbie");
            memo.setContents("영속성 컨텍스트와 트랜잭션 이해하기");

            em.persist(memo); // EntityManager 사용하여 memo 객체를 영속성 컨텍스트에 저장합니다.

            et.commit(); // 오류가 발생하지 않고 정상적으로 수행되었다면 commit 을 호출합니다.
            // commit 이 호출되면서 DB 에 수행한 DB 작업들이 반영됩니다.
        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback(); // DB 작업 중 오류 발생 시 rollback 을 호출합니다.
        } finally {
            em.close(); // 사용한 EntityManager 를 종료합니다.
        }

        emf.close(); // 사용한 EntityManagerFactory 를 종료합니다.
    }


    @Test
    @DisplayName("EntityTransaction 실패 테스트")
    void test2() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 EntityTransaction 을 가져옵니다.

        et.begin(); // 트랜잭션을 시작합니다.

        try { // DB 작업을 수행합니다.

            Memo memo = new Memo(); // 저장할 Entity 객체를 생성합니다.
            memo.setUsername("Robbert");
            memo.setContents("실패 케이스");

            em.persist(memo); // EntityManager 사용하여 memo 객체를 영속성 컨텍스트에 저장합니다.

            et.commit(); // 오류가 발생하지 않고 정상적으로 수행되었다면 commit 을 호출합니다.
            // commit 이 호출되면서 DB 에 수행한 DB 작업들이 반영됩니다.
        } catch (Exception ex) {
            System.out.println("식별자 값을 넣어주지 않아 오류가 발생했습니다.");
            ex.printStackTrace();
            et.rollback(); // DB 작업 중 오류 발생 시 rollback 을 호출합니다.
        } finally {
            em.close(); // 사용한 EntityManager 를 종료합니다.
        }

        emf.close(); // 사용한 EntityManagerFactory 를 종료합니다.
    }
    @Test
    @DisplayName("merge() : 저장")
    void test5() {
        EntityTransaction et = em.getTransaction();

        et.begin();

        try {

            Memo memo = new Memo();
            memo.setId(3L);
            memo.setUsername("merge()");
            memo.setContents("merge() 저장");

            System.out.println("merge() 호출");
            Memo mergedMemo = em.merge(memo);

            System.out.println("em.contains(memo) = " + em.contains(memo));
            System.out.println("em.contains(mergedMemo) = " + em.contains(mergedMemo));

            System.out.println("트랜잭션 commit 전");
            et.commit();
            System.out.println("트랜잭션 commit 후");

        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("merge() : 수정")
    void test6() {
        EntityTransaction et = em.getTransaction();

        et.begin();

        try {

            Memo memo = em.find(Memo.class, 3);
            System.out.println("memo.getId() = " + memo.getId());
            System.out.println("memo.getUsername() = " + memo.getUsername());
            System.out.println("memo.getContents() = " + memo.getContents());

            System.out.println("em.contains(memo) = " + em.contains(memo));

            System.out.println("detach() 호출");
            em.detach(memo); // 준영속 상태로 전환
            System.out.println("em.contains(memo) = " + em.contains(memo));

            System.out.println("준영속 memo 값 수정");
            memo.setContents("merge() 수정");

            System.out.println("\n merge() 호출");
            Memo mergedMemo = em.merge(memo);
            System.out.println("mergedMemo.getContents() = " + mergedMemo.getContents());

            System.out.println("em.contains(memo) = " + em.contains(memo));
            System.out.println("em.contains(mergedMemo) = " + em.contains(mergedMemo));

            System.out.println("트랜잭션 commit 전");
            et.commit();
            System.out.println("트랜잭션 commit 후");

        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}