PREFIX  col: <%s>
PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
SELECT ?asset ?assettype ?status ?attributetype ?attribute {
  { ?domain rdf:type <%s>
    { ?asset col:assets_in_domain_relation ?domain
      { ?asset rdf:type ?assettype
        FILTER(?assettype != col:ignore)
      }
      OPTIONAL { ?asset col:status ?status }
      OPTIONAL {?asset ?attributetype ?attribute FILTER(isliteral(?attribute))
         FILTER(?attributetype != col:ignore)
      }
    }
  }
}