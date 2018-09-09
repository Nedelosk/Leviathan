package leviathan.parser;

/*public enum  GSSParser implements ISelectiveResourceReloadListener {
	INSTANCE;

	public static final String FOLDER = "data/gui";
	public static final Map<ResourceLocation, IGuiStyleSheet> STYLE_SHEETS = new HashMap<>();

	public void parseFiles(IResourceManager resourceManager){
		//ForgeHooksClient
	}

	public void parseClient(ResourceLocation styleLocation){
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(styleLocation);
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
				//GuiParser.parse(reader).forEach(consumer);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public void parse(Reader reader) throws IOException{
		StreamTokenizer tokenizer = new StreamTokenizer(reader);
		tokenizer.slashSlashComments(true);
		tokenizer.eolIsSignificant(false);
		tokenizer.quoteChar('\'');
		tokenizer.parseNumbers();
	}

	public void parseFile(StreamTokenizer tokenizer) throws IOException{
		IGuiStyleSheet styleSheet = new GuiStyleSheet();
		for(int token; (token = tokenizer.nextToken()) != TT_EOF;){

		}
	}


	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
		if(!resourcePredicate.test(JEGResourceType.GUI)){
			return;
		}
		STYLE_SHEETS.clear();
	}
}*/
