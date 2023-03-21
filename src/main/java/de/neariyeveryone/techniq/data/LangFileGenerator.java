package de.neariyeveryone.techniq.data;

import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.item.Items;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class LangFileGenerator extends LanguageProvider {

    public LangFileGenerator(DataGenerator output) {
        super(output, TechniqConstants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(String.format("itemGroup.%s.tab", TechniqConstants.MOD_ID), capitalize(TechniqConstants.MOD_ID));
        Items.ITEMS.getEntries().forEach(item -> add(item.get(), getName(item.getId().getPath())));
    }

    private String getName(String id) {
        var words = id.split("_");
        StringBuilder toReturn = new StringBuilder(capitalize(words[0]));
        if (words.length > 1)
            for (int i = 1; i < words.length; i++)
                toReturn.append(String.format(" %s", capitalize(words[i])));
        return toReturn.toString();
    }

    private String capitalize(String word) {
        return String.valueOf(word.charAt(0)).toUpperCase() + word.substring(1).toLowerCase();
    }
}
