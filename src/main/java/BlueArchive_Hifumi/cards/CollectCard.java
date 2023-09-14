package BlueArchive_Hifumi.cards;

public interface CollectCard {
    default public int expectCollect(){return 0;};
    default public void triggerWhenCollect(){};
}
