package alation.talend.mde.transformations;

import alation.talend.io.Utils;
import alation.talend.mde.types.*;
import alation.talend.resources.Constants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * This class can evaluate Talend's transformation expressions
 */
public class ExpressionEngine {

    private static ProcessType processType;

    public ExpressionEngine(ProcessType processType) {
        this.processType = processType;
    }

  public static String extractFromTransformation(Node node) {
    if (node == null || processType == null || !node.isTransformationNode()) return null;
    if (node.getComponentName().equals(Constants.SUPPORTED_TRANSFORMATIONS.tFilterRow.name())) {
      return extractTFilterRow(node);
    }
    if (node.getComponentName().equals(Constants.SUPPORTED_TRANSFORMATIONS.tAggregateRow.name())) {
      return extractAggregateRow(node);
    }
    if (node.getComponentName().equals(Constants.SUPPORTED_TRANSFORMATIONS.tSortRow.name())) {
      return extractSortRow(node);
    }
    if (node.getComponentName().equals(Constants.SUPPORTED_TRANSFORMATIONS.tReplace.name())) {
      return extractReplace(node);
    }
    if (node.getComponentName().equals(Constants.SUPPORTED_TRANSFORMATIONS.tFilterColumns.name())) {
      return extractFilterColumns(node);
    }
    if (node.getComponentName().equals(Constants.SUPPORTED_TRANSFORMATIONS.tConvertType.name())) {
      return extractConvertType(node);
    }
    if (node.getComponentName().equals(Constants.SUPPORTED_TRANSFORMATIONS.tSampleRow.name())) {
      return extractSampleRow(node);
    }
    if (node.getComponentName().equals(Constants.SUPPORTED_TRANSFORMATIONS.tJoin.name())) {
      return extractJoinRow(node);
    }

        return null;
    }

    /**
     * Extract the expression from the tFilterRow component
     *
     * @param node The node object that contains the parameters.
     * @return The expression that is being returned is the expression that is being used in the tFilterRow
     * component.
     */
    public static String extractTFilterRow(Node node) {
        /*
         *  <node componentName="tFilterRow" ....
         *       <elementParameter field="TABLE" name="CONDITIONS">
         *          <elementValue elementRef="INPUT_COLUMN" value="identifier"/>
         *          <elementValue elementRef="FUNCTION" value=""/>
         *          <elementValue elementRef="OPERATOR" value=">"/>
         *          <elementValue elementRef="RVALUE" value="200"/>
         *        </elementParameter>
         */
        ElementParameter condition =
                node.getElementParameter().stream()
                        .filter(
                                elementParameter ->
                                        Constants.STR_TABLE.equals(elementParameter.getField())
                                                && Constants.STR_CONDITIONS.equals(elementParameter.getName()))
                        .findFirst()
                        .orElse(null);

        if (condition == null) return null;

        List<ElementValue> function = condition.filterByElementRef(Constants.STR_FUNCTION);
        List<ElementValue> inputColumn = condition.filterByElementRef(Constants.STR_INPUT_COLUMN);
        List<ElementValue> operator = condition.filterByElementRef(Constants.STR_OPERATOR);
        List<ElementValue> rValue = condition.filterByElementRef(Constants.STR_RVALUE);
        /*
         * Expected expression = some_function(input_column) operator rvalue
         * */
        ;
        String expression = Constants.EMPTY;
        for (int i = 0; i < operator.size(); i++) {
            String functionStr = function.get(i).getValue();
            if (functionStr != null && functionStr.trim().length() > 0) {
                expression += functionStr + Constants.STR_OPEN_BRACKET;
            }
            expression += inputColumn.get(i).getValue();
            if (functionStr != null && functionStr.trim().length() > 0) {
                expression += Constants.STR_CLOSE_BRACKET;
            }
            expression += Constants.SPACE + operator.get(i).getValue() + Constants.SPACE;
            expression += rValue.get(i).getValue();
        }
        return expression;
    }

    /**
     * It extracts the sample row expression from the node
     *
     * @param node the node that is being processed
     * @return The sample expression is being returned.
     */
    private static String extractSampleRow(Node node) {
        if (node == null) return null;
        ElementParameter infoRange = node.getElementParameter().stream()
                .filter(elementParameter -> Constants.STR_RANGE.equals(elementParameter.getName()))
                .findFirst().orElse(null);
        return getSampleRowExpression(infoRange.getValue());
    }

    /**
     * It takes a string of comma separated numbers and returns a string of comma separated numbers with
     * the word "line" in front of each number
     *
     * @param value the value of the sample row expression
     * @return The sample expression is being returned.
     */
    private static String getSampleRowExpression(String value) {
        if (StringUtils.isEmpty(value)) return Constants.EMPTY;
        String sampleRowExpression = Constants.STR_SELECT;
        Utils.getPlainString(value);
        String[] tokens = value.split(Constants.SYMBOL_COMMA);
        for (String token: tokens) {
            sampleRowExpression += Constants.STR_ROW_LINE;
            if (token.contains(String.valueOf(Constants.CHAR_DOT))){
                token = token.substring(0, token.indexOf(Constants.CHAR_DOT)) + Constants.STR_TO + token.substring(token.lastIndexOf(Constants.CHAR_DOT)+1);
            }
            sampleRowExpression += token.replaceAll(Constants.SYMBOL_QUOT, Constants.EMPTY) + Constants.SYMBOL_COMMA;
        }
        return sampleRowExpression.equals(Constants.STR_SELECT)
                ? sampleRowExpression.replaceFirst(Constants.STR_SELECT, Constants.EMPTY)
                : sampleRowExpression.substring(0, sampleRowExpression.lastIndexOf(Constants.SYMBOL_COMMA));
    }

    /**
     * This function extracts the join row expression from the node
     *
     * @param node the node that is being processed
     * @return The row of the table that is being joined.
     */
    private static String extractJoinRow(Node node) {
        List<ElementValue> inputColumns= node.getElementParameter().stream()
                .filter(elementParameter -> Constants.STR_JOIN_KEY.equals(elementParameter.getName())).map(ElementParameter::getElementValue)
                .findFirst().orElse(null).stream().filter(val -> Constants.STR_INPUT_COLUMN.equals(val.getElementRef())).collect(Collectors.toList());
        return getJoinExpression(node, inputColumns);
    }

    /**
     * It extracts the sort expression from the node's element parameters
     *
     * @param node The node object that contains the parameters of the component.
     * @param inputColumns The element value object that contains the input columns of the join operation.
     * @return The join expression.
     */
    @Nullable
    private static String getJoinExpression(Node node, List<ElementValue> inputColumns) {
        ElementParameter uniqueNameElementParameter = getUniqueNameElementParameter(node);
        if (uniqueNameElementParameter == null) return null;
        List<String> joinSources = processType.getConnection().stream()
                .filter(connection -> uniqueNameElementParameter.getValue().equals(connection.getTarget()))
                .map(Connection::getSource).sorted().collect(Collectors.toList());
        List<String> sqlQueries = getJoinTableNames(joinSources);
        if (CollectionUtils.isEmpty(sqlQueries)) return null;
        String expression = Constants.JOIN_EXPRESSION.replace(Constants.Arguments.ARGUMENT_1.getValue(), Utils.getPlainString(sqlQueries.get(0)))
                .replace(Constants.Arguments.ARGUMENT_2.getValue(), Utils.getPlainString(sqlQueries.get(1)))
                .replace(Constants.Arguments.ARGUMENT_3.getValue(), String.join(Constants.SYMBOL_COMMA_SPACE, inputColumns.stream().map(ElementValue::getValue).collect(Collectors.toList())))
                .replace(Constants.Arguments.ARGUMENT_4.getValue(), getLookUpColumns(node));
        return expression;
    }

    /**
     * Extracts the type of the conversion expression from the node
     *
     * @param node the node to be converted
     * @return The lookup columns is being returned.
     */
  private static String getLookUpColumns(Node node) {
    String lookupExpression = Constants.EMPTY;
    ElementParameter lookupParameter = node.getElementParameter().stream()
            .filter(elementParameter -> Constants.STR_TABLE.equals(elementParameter.getField()) && Constants.STR_LOOKUP_COLS.equals(elementParameter.getName()))
            .findFirst().orElse(null);
    ElementParameter useLookupCols= node.getElementParameter().stream()
            .filter(elementParameter -> Constants.STR_USE_LOOKUP_COLS.equals(elementParameter.getName()))
            .findFirst().orElse(null);
    List<ElementValue> lookupElements = Boolean.parseBoolean(useLookupCols.getValue()) ? lookupParameter.filterByElementRef(Constants.STR_LOOKUP_COLUMN) : new ArrayList<>();
    return CollectionUtils.isEmpty(lookupElements) ? Constants.STR_NA : String.join(Constants.SYMBOL_COMMA_SPACE, lookupElements.stream().map(ElementValue::getValue).collect(Collectors.toList()));
  }

    /**
     * Extracts the type of the conversion expression from the node
     *
     * @param joinSources the name of connected sources
     * @return The table names of join operation is being returned.
     */
  private static List<String> getJoinTableNames(List<String> joinSources) {
      List<Node> sourceNodes = new ArrayList<>();
      processType.getInputNodes().forEach(node -> {
          node.getElementParameter().forEach(element -> {
              if (joinSources.contains(element.getValue())){
                  sourceNodes.add(node);
              }
          });
      });
      List<String> sqlList = new ArrayList<>();
      sourceNodes.forEach(node1 -> node1.getElementParameter().forEach(elementParameter -> {
          if (Constants.STR_DBTABLE.equals(elementParameter.getField()) && Constants.STR_TABLE.equals(elementParameter.getName())){
              sqlList.add(elementParameter.getValue());
          }
      }));
      return sqlList;
  }
    /**
     * Extracts the type of the conversion expression from the node
     *
     * @param node the node to be converted
     * @return The convertType is being returned.
     */
      private static String extractConvertType(Node node) {
    return getConvertTypeExpression(node);
  }

    /**
     * It extracts the sort expression from the node's element parameters
     *
     * @param node The node object that contains the parameters of the component.
     * @return The convert type expression.
     */
  private static String getConvertTypeExpression(Node node) {
      Node sourceNode = getSourceNode(processType, node);
      if (sourceNode == null) return null;
      Metadata targetMetadata = node.getMetadata().stream()
              .filter(metadata -> Constants.STR_FLOW.equals(metadata.getConnector()))
              .findFirst().orElse(null);
      Metadata sourceMetadata = sourceNode.getMetadata().stream()
              .filter(metadata -> Constants.STR_FLOW.equals(metadata.getConnector()))
              .findFirst().orElse(null);
      if (targetMetadata == null && sourceMetadata == null) return null;
      List<Column> targetCols = targetMetadata.getColumn();
      List<Column> sourceCols = sourceMetadata.getColumn();
      String convertTypeExpression = Constants.EMPTY;
      int targetLen = targetCols.size();
      for (int i = 0; i < targetLen; i++) {
          if(!targetCols.get(i).getType().equals(sourceCols.get(i).getType())) {
              convertTypeExpression += Constants.CONVERT_TYPE_EXPRESSION
                      .replace(Constants.Arguments.ARGUMENT_1.getValue(), targetCols.get(i).getName())
                      .replace(Constants.Arguments.ARGUMENT_2.getValue(), targetCols.get(i).getType().replace(Constants.STR_ID, Constants.EMPTY));
          }
      }
      return convertTypeExpression;
  }

    /**
     * It extracts the sort expression from the node's element parameters
     *
     * @param processType The proessType object that contains the metadata of the components.
     * @param node The node object that contains the parameters of the component.
     * @return The source input node of the connection.
     */
  private static Node getSourceInputNode(ProcessType processType, Node node) {
      Node sourceNode = null;
      ElementParameter uniqueElement = getUniqueNameElementParameter(node);
      if (uniqueElement == null) return null;
      AtomicReference<String> source = new AtomicReference<>();
      Connection filteredConnection = processType.getConnection().stream()
              .filter(connection -> uniqueElement.getValue().equals(connection.getTarget()))
              .findFirst().orElse(null);
      if (filteredConnection == null) return null;
      List<Connection> connections = processType.getConnection();
      int connectionsLen = connections.size();
      for (int i = connectionsLen-1; i >= 0 ; i--){
          source.set(filteredConnection.getSource());
          if (source.get().contains(Constants.STR_Input)){
              sourceNode = IterableUtils.find(processType.getInputNodes(),
                      new Predicate<Node>() {
                          @Override
                          public boolean evaluate(Node node) {
                              ElementParameter elp = node.getElementParameter().stream()
                                      .filter(ep -> Constants.STR_UNIQUE_NAME.equals(ep.getName()) && source.get().equals(ep.getValue()))
                                      .findFirst().orElse(null);
                              return elp != null;
                          }
                      });
              break;
          } else {
              filteredConnection = getPreviousConnection(source.get());
              if (i == 0) {i = connectionsLen-1;}
          }
      }
      return sourceNode;
  }
    /**
     * It extracts the sort expression from the node's element parameters
     *
     * @param processType The proessType object that contains the metadata of the components.
     * @param node The node object that contains the parameters of the component.
     * @return The source node matches to the connection.
     */
  private static Node getSourceNode(ProcessType processType, Node node){
      String uniqueNameValue = getUniqueNameElementParameter(node).getValue();
      Connection filteredConnection = processType.getConnection().stream()
              .filter(connection -> uniqueNameValue.equals(connection.getTarget()))
              .findFirst().orElse(null);
      if (filteredConnection == null) return null;
      List<Node> nodes = filteredConnection.getSource().contains(Constants.STR_Input) ? processType.getInputNodes() : processType.getTransformationNodes();
      Node sourceNode = IterableUtils.find(nodes, new Predicate<Node>() {
          @Override
          public boolean evaluate(Node node) {
              ElementParameter elp = node.getElementParameter().stream()
                      .filter(ele -> Constants.STR_UNIQUE_NAME.equals(ele.getName()) && filteredConnection.getSource().equals(ele.getValue()))
                      .findFirst().orElse(null);
              return elp != null;
          }
      });
      return sourceNode;
  }

    @NotNull
    private static Connection getPreviousConnection(String source) {
        return processType.getConnection().stream().filter(connection -> source.equals(connection.getTarget())).findFirst().get();
    }

    /**
     * It extracts the sort expression from the node's element parameters
     *
     * @param node The node object that contains the parameters of the component.
     * @return The sort expression
     */
    private static String extractSortRow(Node node) {
        ElementParameter condition = node.getElementParameter().stream()
                .filter(elementParameter ->
                        Constants.STR_TABLE.equals(elementParameter.getField()) && Constants.STR_CRITERIA.equals(elementParameter.getName()))
                .findFirst().orElse(null);
        if (condition == null) return null;
        return getSortExpression(condition);
    }

    /**
     * It takes a condition element and returns a string that represents the sort expression
     *
     * @param condition The condition element from the XML file.
     * @return A string that is the sort expression.
     */
    private static String getSortExpression(ElementParameter condition) {
        List<ElementValue> colName = condition.filterByElementRef(Constants.STR_COLNAME);
        List<ElementValue> order = condition.filterByElementRef(Constants.STR_ORDER);
        String sortExpression = Constants.EMPTY;
		int colLen = colName.size();
        for (int i = 0; i < colLen; i++) {
            sortExpression += Constants.SORT_EXPRESSION
                    .replace(Constants.Arguments.ARGUMENT_1.getValue(), colName.get(i).getValue())
                    .replace(Constants.Arguments.ARGUMENT_2.getValue(), order.get(i).getValue());
        }
        return sortExpression;
    }

    /**
     * It extracts the replace expression from the node
     *
     * @param node The node that we're currently processing.
     * @return The replace expression
     */
    private static String extractReplace(Node node) {
        ElementParameter condition =
                node.getElementParameter().stream()
                        .filter(
                                elementParameter ->
                                        Constants.STR_TABLE.equals(elementParameter.getField())
                                                && Constants.STR_SUBSTITUTIONS.equals(elementParameter.getName()))
                        .findFirst()
                        .orElse(null);

        if (condition == null) return null;

        return getReplaceExpression(condition);
    }

    /**
     * It takes a condition element parameter, filters out the input column, search value, replace value
     * and case sensitive values, and then returns a replace expression
     *
     * @param condition The condition element that contains the parameters for the replace expression.
     * @return A string that is the replace expression.
     */
    private static String getReplaceExpression(ElementParameter condition) {
        List<ElementValue> inputColumn = condition.filterByElementRef(Constants.STR_INPUT_COLUMN);
        List<ElementValue> searchValue = condition.filterByElementRef(Constants.STR_SEARCH_PATTERN);
        List<ElementValue> replaceValue = condition.filterByElementRef(Constants.STR_REPLACE_STRING);
        List<ElementValue> caseSensitive = condition.filterByElementRef(Constants.STR_CASE_SENSITIVE);
        String replaceExpression = Constants.EMPTY;
        String input;
		int searchLen = searchValue.size();
        for (int i = 0; i < searchLen; i++) {
            replaceExpression += Constants.NEWLINE + Constants.REPLACE_EXPRESSION;
            if (StringUtils.isNotEmpty((input = inputColumn.get(i).getValue()))) {
                replaceExpression = replaceExpression.replace(Constants.Arguments.ARGUMENT_1.getValue(), input);
            }
            if (StringUtils.isNotEmpty((input = searchValue.get(i).getValue().replaceAll(Constants.SYMBOL_QUOT_COLON, Constants.EMPTY)))) {
                replaceExpression = replaceExpression.replace(Constants.Arguments.ARGUMENT_2.getValue(), input);
            }
            if (StringUtils.isNotEmpty((input = replaceValue.get(i).getValue().replaceAll(Constants.SYMBOL_QUOT_COLON, Constants.EMPTY)))) {
                replaceExpression = replaceExpression.replace(Constants.Arguments.ARGUMENT_3.getValue(), input);
            }
            if (StringUtils.isNotEmpty((input = caseSensitive.get(i).getValue()))) {
                replaceExpression += Constants.SPACE + (Boolean.parseBoolean(input) ? Constants.STR_CASE_SENSITIVE : Constants.STR_CASE_INSENSITIVE);
            }

        }
        return replaceExpression;
    }

    /**
     * This function extracts the filter columns from the node
     *
     * @param node The node that is being processed.
     */
  public static String extractFilterColumns(Node node) {
    if (processType == null || node == null) return null;
    return getFilterColumnsExpression(node);
  }

  private static String getFilterColumnsExpression(Node node) {
      Node sourceNode = getSourceInputNode(processType, node);
      // sourceColumns
      if (sourceNode == null) return null;
      List<Column> sourceColumns = sourceNode.getMetadata().stream().filter(meta -> Constants.STR_FLOW.equals(meta.getConnector()))
              .map(Metadata::getColumn).findFirst().orElse(null);
      List<String> sourceColNames = sourceColumns.stream().map(Column::getName).collect(Collectors.toList());
      // targetColumns
      List<Column> targetColumns = node.getMetadata().stream().filter(meta -> Constants.STR_FLOW.equals(meta.getConnector()))
              .map(Metadata::getColumn).findFirst().orElse(null);
      List<String> targetColNames = targetColumns.stream().map(Column::getName).collect(Collectors.toList());
      int sourceColNamesLen = sourceColNames.size();
      int targetColNamesLen = targetColNames.size();
      String filterColExpression = Constants.EMPTY;
      if (targetColNamesLen > sourceColNamesLen){
          targetColNames.removeAll(sourceColNames);
          filterColExpression = "Extra columns were added: " + String.join(Constants.SYMBOL_COMMA_SPACE, targetColNames);
      }
      if (sourceColNamesLen > targetColNamesLen){
          sourceColNames.removeAll(targetColNames);
          filterColExpression = "Unwanted columns were removed: " + String.join(Constants.SYMBOL_COMMA_SPACE, sourceColNames);
      }
      if (sourceColNamesLen == targetColNamesLen){
          List<String> orderedCols = new ArrayList<>();
          for (int i = 0; i < sourceColNamesLen; i++) {
              if (sourceColNames.get(i).equals(targetColNames.get(i))){
                  orderedCols.add(targetColNames.get(i));
              }
          }
          filterColExpression = orderedCols.size() > 0 ? "Columns were ordered: " + String.join(Constants.SYMBOL_COMMA_SPACE, orderedCols) : Constants.EMPTY;
      }
      return filterColExpression;
  }

    @NotNull
    private static ElementParameter getUniqueNameElementParameter(Node node) {
        return node.getElementParameter().stream().filter(ele -> ele.getName().equals(Constants.STR_UNIQUE_NAME)).findFirst().orElse(null);
    }

    /**
     * It extracts the aggregate row expression from the node
     *
     * @param node The node object that contains the parameters.
     * @return The return value is a string that contains the operation expression and the groups by
     * expression.
     */
    private static String extractAggregateRow(Node node) {
        ElementParameter condition =
                node.getElementParameter().stream()
                        .filter(
                                elementParameter ->
                                        Constants.STR_TABLE.equals(elementParameter.getField())
                                                && Constants.STR_OPERATIONS.equals(elementParameter.getName()))
                        .findFirst()
                        .orElse(null);

        ElementParameter condition1 =
                node.getElementParameter().stream()
                        .filter(
                                elementParameter ->
                                        Constants.STR_TABLE.equals(elementParameter.getField())
                                                && Constants.STR_GROUPSBY.equals(elementParameter.getName()))
                        .findFirst()
                        .orElse(null);

        return getOperationExpression(condition) + getGroupsByExpression(condition1);
    }

    /**
     * It takes a condition element and returns a string that contains the group by expression
     *
     * @param condition1 The condition element that contains the group by expression
     * @return The groupByExpression is being returned.
     */
    private static String getGroupsByExpression(ElementParameter condition1) {
        List<ElementValue> groupByColumn = condition1.filterByElementRef(Constants.STR_INPUT_COLUMN);
        /*
         * Expected groupsByExpression = some_function(input_column) operator rvalue
         * */
        ;
        String groupsByExpression = Constants.EMPTY;
		int groupByColLen = groupByColumn.size();
        for (int i = 0; i < groupByColLen; i++) {
            String groupByValue = groupByColumn.get(i).getValue();
            if (groupByValue != null && groupByValue.trim().length() > 0) {
                groupsByExpression += Constants.GROUP_BY_EXPRESSION.replace(Constants.Arguments.ARGUMENT_1.getValue(), groupByValue);
            }
        }
        return groupsByExpression;
    }

    /**
     * It takes a condition element and returns a string that contains the operation expression
     *
     * @param condition The condition element from the tMap component.
     * @return The operationExpression is being returned.
     */
    private static String getOperationExpression(ElementParameter condition) {
        List<ElementValue> function = condition.filterByElementRef(Constants.STR_FUNCTION);
        List<ElementValue> inputColumn = condition.filterByElementRef(Constants.STR_INPUT_COLUMN);
        String operationExpression = Constants.EMPTY;
		int functionsLen = function.size();
        for (int i = 0; i < functionsLen; i++) {
            String functionStr = function.get(i).getValue();
            if (functionStr != null && functionStr.trim().length() > 0) {
                operationExpression += Constants.OPERATIONS_EXPRESSION
                        .replace(Constants.Arguments.ARGUMENT_1.getValue(), functionStr.toUpperCase())
                        .replace(Constants.Arguments.ARGUMENT_2.getValue(), inputColumn.get(i).getValue());
            }
        }
        return operationExpression.replaceFirst(Constants.NEWLINE, Constants.EMPTY);
    }
}
