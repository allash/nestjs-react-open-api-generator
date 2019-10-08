package com.my.codegen;

import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.util.SchemaTypeUtil;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.*;
import io.swagger.models.properties.*;
import org.openapitools.codegen.languages.AbstractTypeScriptClientCodegen;
import org.openapitools.codegen.utils.ModelUtils;

import java.util.*;
import java.io.File;

public class TypescriptAxiosCustomGenerator extends AbstractTypeScriptClientCodegen {

  private String tsModelPackage = "";

  public TypescriptAxiosCustomGenerator() {
    super();

    // clear import mapping (from default generator) as TS does not use it
    // at the moment
    importMapping.clear();

    outputFolder = "generated-code/typescript-axios-custom";
    embeddedTemplateDir = templateDir = "typescript-axios-custom";
  }

  @Override
  public String getName() {
    return "typescript-axios-custom";
  }

  @Override
  public String getHelp() {
    return "Generates a TypeScript client library using axios.";
  }

  private static String getRelativeToRoot(String path) {
    StringBuilder sb = new StringBuilder();
    int slashCount = path.split("/").length;
    if (slashCount == 0) {
      sb.append("./");
    } else {
      for (int i = 0; i < slashCount; ++i) {
        sb.append("../");
      }
    }
    return sb.toString();
  }

  @Override
  public void processOpts() {
    super.processOpts();
    tsModelPackage = modelPackage.replaceAll("\\.", "/");
    String tsApiPackage = apiPackage.replaceAll("\\.", "/");

    String modelRelativeToRoot = getRelativeToRoot(tsModelPackage);
    String apiRelativeToRoot = getRelativeToRoot(tsApiPackage);

    additionalProperties.put("tsModelPackage", tsModelPackage);
    additionalProperties.put("tsApiPackage", tsApiPackage);
    additionalProperties.put("apiRelativeToRoot", apiRelativeToRoot);
    additionalProperties.put("modelRelativeToRoot", modelRelativeToRoot);

    supportingFiles.add(new SupportingFile("index.mustache", "", "index.ts"));
    supportingFiles.add(new SupportingFile("baseApi.mustache", "", "base.ts"));
    supportingFiles.add(new SupportingFile("api.mustache", "", "api.ts"));
    supportingFiles.add(new SupportingFile("configuration.mustache", "", "configuration.ts"));
    supportingFiles.add(new SupportingFile("custom.d.mustache", "", "custom.d.ts"));
    supportingFiles.add(new SupportingFile("gitignore", "", ".gitignore"));
  }

  @Override
  public Map<String, Object> postProcessOperationsWithModels(Map<String, Object> objs, List<Object> allModels) {
    objs = super.postProcessOperationsWithModels(objs, allModels);
    Map<String, Object> vals = (Map<String, Object>) objs.getOrDefault("operations", new HashMap<>());
    List<CodegenOperation> operations = (List<CodegenOperation>) vals.getOrDefault("operation", new ArrayList<>());
        /*
            Filter all the operations that are multipart/form-data operations and set the vendor extension flag
            'multipartFormData' for the template to work with.
         */
    operations.stream()
            .filter(op -> op.hasConsumes)
            .filter(op -> op.consumes.stream().anyMatch(opc -> opc.values().stream().anyMatch("multipart/form-data"::equals)))
            .forEach(op -> op.vendorExtensions.putIfAbsent("multipartFormData", true));
    return objs;
  }

  @Override
  protected void addAdditionPropertiesToCodeGenModel(CodegenModel codegenModel, Schema schema) {
    codegenModel.additionalPropertiesType = getTypeDeclaration(ModelUtils.getAdditionalProperties(schema));
    addImport(codegenModel, codegenModel.additionalPropertiesType);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> postProcessModels(Map<String, Object> objs) {
    List<Object> models = (List<Object>) postProcessModelsEnum(objs).get("models");

    for (Object _mo  : models) {
      Map<String, Object> mo = (Map<String, Object>) _mo;
      CodegenModel cm = (CodegenModel) mo.get("model");

      // Deduce the model file name in kebab case
      cm.classFilename = cm.classname.replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase(Locale.ROOT);

      //processed enum names
        cm.imports = new TreeSet(cm.imports);
        // name enum with model name, e.g. StatusEnum => PetStatusEnum
        for (CodegenProperty var : cm.vars) {
          if (Boolean.TRUE.equals(var.isEnum)) {
            var.datatypeWithEnum = var.datatypeWithEnum.replace(var.enumName, cm.classname + var.enumName);
            var.enumName = var.enumName.replace(var.enumName, cm.classname + var.enumName);
          }
        }
        if (cm.parent != null) {
          for (CodegenProperty var : cm.allVars) {
            if (Boolean.TRUE.equals(var.isEnum)) {
              var.datatypeWithEnum = var.datatypeWithEnum.replace(var.enumName, cm.classname + var.enumName);
              var.enumName = var.enumName.replace(var.enumName, cm.classname + var.enumName);
            }
          }
        }
    }

    // Apply the model file name to the imports as well
    for (Map<String, String> m : (List<Map<String, String>>) objs.get("imports")) {
      String javaImport = m.get("import").substring(modelPackage.length() + 1);
      String tsImport = tsModelPackage + "/" + javaImport;
      m.put("tsImport", tsImport);
      m.put("class", javaImport);
      m.put("filename", javaImport.replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase(Locale.ROOT));
    }
    return objs;
  }

  @Override
  public String toModelFilename(String name) {
    return super.toModelFilename(name).replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase(Locale.ROOT);
  }

  @Override
  public String toApiFilename(String name) {
    return super.toApiFilename(name).replaceAll("([a-z0-9])([A-Z])", "$1-$2").toLowerCase(Locale.ROOT);
  }
}